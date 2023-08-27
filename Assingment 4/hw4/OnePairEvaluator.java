package hw4;

import api.Card;
import api.Hand;
import util.SubsetFinder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Evaluator for a hand containing (at least) two cards of the same rank.
 * The number of cards required is two.
 * 
 * The name of this evaluator is "One Pair".
 */
//Note: You must edit this declaration to extend AbstractEvaluator
//or to extend some other class that extends AbstractEvaluator
public class OnePairEvaluator extends AbstractEvaluator
{
  private int ranking;
  private int handSize;
  /**
   * Constructs the evaluator.
   * @param ranking
   *   ranking of this hand
   * @param handSize
   *   number of cards in a hand
   */
  public OnePairEvaluator(int ranking, int handSize)
  {
    // TODO: call appropriate superclass constructor and
    // perform other initialization
    this.ranking = ranking;
    this.handSize = handSize;
  }

  @Override
  public int getRanking() {
    return ranking;
  }

  @Override
  public int handSize() {
    return handSize;
  }

  @Override
  public String getName() {
    return "One Pair";
  }

  @Override
  public int cardsRequired() {
    return 2;
  }

  @Override
  public boolean canSatisfy(Card[] mainCards) {
    Arrays.sort(mainCards);
    boolean satisty =  false;
    if (mainCards.length == cardsRequired()){
      if(mainCards[0].getRank() == mainCards[1].getRank()){
        satisty = true;
      }
    }
    return satisty;
  }

  @Override
  public boolean canSubsetSatisfy(Card[] allCards) {
    if(allCards.length < cardsRequired()){
      return false;
    }

    if(getValidSubsets(allCards).size() > 0){
      return true;
    }
    return false;
  }

  private ArrayList<int[]> getValidSubsets(Card[] allCards){
    ArrayList<int[]> validSubsets = new ArrayList<>();
    ArrayList<int[]> subsets =  SubsetFinder.findSubsets(allCards.length, cardsRequired());
    for(int i = 0; i < subsets.size(); i++){
      Card[] subsetCards = cardsMatchingSubsets(allCards, subsets.get(i));
      if(subsetCards[0].getRank() == subsetCards[1].getRank() &&
         subsetCards[0].getSuit() != subsetCards[1].getSuit()
        ){
        validSubsets.add(subsets.get(i));
      }
    }
    return validSubsets;

  }

//  private Card[] cardsMatchingSubsets(Card[] allcards, int[] subset){
//    Card[] subsetCards = new Card[subset.length];
//    for(int i = 0; i < subset.length; i++){
//      subsetCards[i] = allcards[subset[i]];
//    }
//    return subsetCards;
//  }

  @Override
  public Hand createHand(Card[] allCards, int[] subset) {
    ArrayList <Card> mainCards = new ArrayList<>();
    ArrayList <Card> sideCards = new ArrayList<>();
    for(int i = 0; i < cardsRequired() ; i++){
      mainCards.add(allCards[subset[i]]);
    }
    Card[] mainArr = new Card[mainCards.size()];
    for(int i = 0; i < mainArr.length; i++){
      mainArr[i] = mainCards.get(i);
    }

    for(int i = 0; i< allCards.length; i++) {
      boolean indexPresentInSubset = false;
      for(int j = 0; j<subset.length; j++) {
        if(subset[j] == i) {
          indexPresentInSubset = true;
          break;
        }
      }
      if(!indexPresentInSubset) {
        sideCards.add(allCards[i]);
      }
    }

    Card[] sideArr = new Card[sideCards.size()];
    for(int i = 0; i < sideArr.length; i++){
      sideArr[i] = sideCards.get(i);
    }

    boolean valid = canSatisfy(mainArr);
    if(!valid){
      return null;
    }

    Arrays.sort(sideArr);

    Hand hand  = new Hand(mainArr, sideArr, this);
    if(allCards.length < handSize()){
      return null;
    }
    return hand;
  }

  private ArrayList<Card[]> getPossibleCardSubsets(ArrayList<int[]> subsets, Card[] allCards){
    ArrayList<Card[]> cards =  new ArrayList<>();
    for(int i = 0; i < subsets.size(); i++){
      int[] subset = subsets.get(i);
      Card[] card = new Card[subset.length];
      for(int j = 0; j < subset.length; j++){
        card[j] = allCards[subset[j]];
      }
      cards.add(card);
    }
    return cards;
  }

  @Override
  public Hand getBestHand(Card[] allCards) {
    ArrayList<int[]> validSubsets = getValidSubsets(allCards);
    ArrayList<Card[]> validCards = getPossibleCardSubsets(validSubsets, allCards);

    Hand[] hands =  new Hand[validCards.size()];
    if (validSubsets.size() > 0){
      for(int i = 0; i < validCards.size(); i++) {

        Hand hand = createHand(allCards, validSubsets.get(i));
        hands[i] = hand;
      }
    }

    if(hands.length < 1){
      return null;
    }

    Arrays.sort(hands);
    Hand bestHand = hands[0];

    if(!canSatisfy(bestHand.getMainCards()) || bestHand.getMainCards().length + bestHand.getSideCards().length < handSize() || validSubsets.size() == 0 ) {
      return null;
    }

    return bestHand;
  }
}
