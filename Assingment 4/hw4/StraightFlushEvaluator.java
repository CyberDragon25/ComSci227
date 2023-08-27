package hw4;

import api.Card;
import api.Hand;
import api.Suit;
import util.SubsetFinder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Evaluator for a hand consisting of a "straight" in which the
 * card ranks are consecutive numbers AND the cards all
 * have the same suit.  The number of required 
 * cards is equal to the hand size.  An ace (card of rank 1) 
 * may be treated as the highest possible card or as the lowest
 * (not both) To evaluate a straight containing an ace it is
 * necessary to know what the highest card rank will be in a
 * given game; therefore, this value must be specified when the
 * evaluator is constructed.  In a hand created by this
 * evaluator the cards are listed in descending order with high 
 * card first, e.g. [10 9 8 7 6] or [A K Q J 10], with
 * one exception: In case of an ace-low straight, the ace
 * must appear last, as in [5 4 3 2 A]
 * 
 * The name of this evaluator is "Straight Flush".
 */
//Note: You must edit this declaration to extend AbstractEvaluator
//or to extend some other class that extends AbstractEvaluator
public class StraightFlushEvaluator extends AbstractEvaluator
{
  private int ranking;
  private int handSize;
  private int maxCardRank;
  /**
   * Constructs the evaluator. Note that the maximum rank of
   * the cards to be used must be specified in order to 
   * correctly evaluate a straight with ace high.
   * @param ranking
   *   ranking of this hand
   * @param handSize
   *   number of cards in a hand
   * @param maxCardRank
   *   largest rank of any card to be used
   */
  public StraightFlushEvaluator(int ranking, int handSize, int maxCardRank)
  {
    // TODO: call appropriate superclass constructor and 
    // perform other initialization
    this.ranking = ranking;
    this.handSize = handSize;
    this.maxCardRank = maxCardRank;
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
    return "Straight Flush";
  }

  @Override
  public int cardsRequired() {
    return handSize;
  }

  @Override
  public boolean canSatisfy(Card[] mainCards) {
    return mainCards.length == handSize;
  }

  @Override
  public boolean canSubsetSatisfy(Card[] allCards) {
    Arrays.sort(allCards);
    Card[] sortedCards = allCards;
    if(getValidSubsets(allCards).size() > 0){
      return true;
    } else {
      return false;
    }
  }

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

  private ArrayList<int[]> getValidSubsets(Card[] allCards) {
    ArrayList<int[]> validSubsets = new ArrayList<>();
    ArrayList<int[]> subsets = SubsetFinder.findSubsets(allCards.length, cardsRequired());

    for(int i = 0; i < subsets.size(); i++){
      int[] subset = subsets.get(i);
      Card[] highAceCards = subsetToCards(subset, allCards);
      setRankForAce(highAceCards);
      Arrays.sort(highAceCards);
      if(isStraightSet(highAceCards)){
        validSubsets.add(cardsToSubset(highAceCards, allCards));
      } else {
        // try the lowest straight ace
        Card[] lowAceCards = subsetToCards(subset, allCards);
        for(int j = 0; j < lowAceCards.length; j++){
          if(lowAceCards[j].getRank() == 1){
            Card c  = new Card(-1, lowAceCards[j].getSuit());
            lowAceCards[j] = c;
          }
        }
        Arrays.sort(lowAceCards);
        for(int j = 0; j < lowAceCards.length; j++){
          if(lowAceCards[j].getRank() == -1){
            Card c  = new Card(1, lowAceCards[j].getSuit());
            lowAceCards[j] = c;
          }
        }
        if(isStraightSet(lowAceCards)){
          validSubsets.add(cardsToSubset(lowAceCards, allCards));
        }
      }


    }
    return validSubsets;
  }


  private boolean isStraightSet(Card[] cards){
    for(int i = 1; i < cards.length; i++){
      int cardRank = cards[i].getRank();
      int previousRank = cards[i - 1].getRank();
      if((previousRank - cardRank != 1) || cards[i].getSuit() != cards[i - 1].getSuit()){
        return false;
      }
    }
    return true;
  }

  // changing rank of ace card
  private void setRankForAce(Card[] cards){
    for(int i = 0; i < cards.length; i++) {
      if(cards[i].getRank() == 1){
        Card c = cards[i];
        Card c1 = new Card(maxCardRank + 1, c.getSuit());
        cards[i] = c1;
      }
    }
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
