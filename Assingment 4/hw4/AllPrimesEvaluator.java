package hw4;

import api.Card;
import api.Hand;
import api.IEvaluator;
import util.SubsetFinder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Evaluator for a hand in which the rank of each card is a prime number.
 * The number of cards required is equal to the hand size. 
 * 
 * The name of this evaluator is "All Primes".
 */
//Note: You must edit this declaration to extend AbstractEvaluator
//or to extend some other class that extends AbstractEvaluator
public class AllPrimesEvaluator extends AbstractEvaluator
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
  public AllPrimesEvaluator(int ranking, int handSize)
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
    return "All Primes";
  }

  @Override
  public int cardsRequired() {
    return handSize();
  }

  private boolean isPrime(Card card){
    boolean isPrime = true;
    if(card.getRank() <= 1){
      isPrime = false;
    } else {
      for (int i = 2; i <= Math.sqrt(card.getRank()); i++) {
        if (card.getRank() % i == 0) {
          isPrime = false;
          break;
        }
      }
    }

    return isPrime;

  }

  @Override
  public boolean canSatisfy(Card[] mainCards) {
    boolean satisty =  false;
    if (mainCards.length == cardsRequired()){
      for(int i = 0; i < mainCards.length; i++){
        if(!isPrime(mainCards[i])){
          return satisty;
        }
      }
      satisty = true;
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
    ArrayList<int[]> allSubsets =  SubsetFinder.findSubsets(allCards.length, handSize());
    for(int i = 0; i < allSubsets.size(); i++){
      Card[] subsetCards = cardsMatchingSubsets(allCards, allSubsets.get(i));
      if(cardArrayPrime(subsetCards)){
        validSubsets.add(allSubsets.get(i));
      }
    }
    return validSubsets;

  }

  private boolean cardArrayPrime(Card[] cards){
    boolean allPrime = true;
    for(int i = 0; i < cards.length; i++){
      if(!isPrime(cards[i])){
        allPrime = false;
        break;
      }
    }
    return allPrime;
  }

 // returns array of cards for a subset
//  private Card[] cardsMatchingSubsets(Card[] allcards, int[] subset){
//    Card[] subsetCards = new Card[subset.length];
//    for(int i = 0; i < subset.length; i++){
//      subsetCards[i] = allcards[subset[i]];
//    }
//    return subsetCards;
//  }


  @Override
  public Hand createHand(Card[] allCards, int[] subset) {
    ArrayList<Card> mainCards = new ArrayList<>();
    ArrayList <Card> sideCards = new ArrayList<>();
    for(int i = 0; i < cardsRequired() ; i++){
      mainCards.add(allCards[subset[i]]);
    }
    Card[] mainArr = new Card[mainCards.size()];
    for(int i = 0; i < mainArr.length; i++){
      mainArr[i] = mainCards.get(i);
    }

    for(int i = 0; i < allCards.length ; i++){
      if (!mainCards.contains(allCards[i])){
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
