package hw4;

import api.Card;
import api.Hand;
import util.SubsetFinder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Evaluator for a generalized full house.  The number of required
 * cards is equal to the hand size.  If the hand size is an odd number
 * n, then there must be (n / 2) + 1 cards of the matching rank and the
 * remaining (n / 2) cards must be of matching rank. In this case, when constructing
 * a hand, the larger group must be listed first even if of lower rank
 * than the smaller group</strong> (e.g. as [3 3 3 5 5] rather than [5 5 3 3 3]).
 * If the hand size is even, then half the cards must be of matching 
 * rank and the remaining half of matching rank.  Any group of cards,
 * all of which are the same rank, always satisfies this
 * evaluator.
 * 
 * The name of this evaluator is "Full House".
 */
//Note: You must edit this declaration to extend AbstractEvaluator
//or to extend some other class that extends AbstractEvaluator
public class FullHouseEvaluator extends AbstractEvaluator
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
  public FullHouseEvaluator(int ranking, int handSize)
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
    return "Full House";
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
    if(allCards.length < cardsRequired()){
      return false;
    }
    if(getValidSubsets(allCards).size() > 0){
      return true;
    }
    return false;
  }

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

    Hand hand  = new Hand(mainArr, null, this);
    if(allCards.length < handSize()){
      return null;
    }
    return hand;
  }

  private ArrayList<int[]> getValidSubsets(Card[] allCards){
    ArrayList<int[]> validSubsets = new ArrayList<>();
    ArrayList<int[]> subsets =  SubsetFinder.findSubsets(allCards.length, cardsRequired());
    //This is for sameRankCards
    ArrayList<int[]> sameRankCards =  new ArrayList<>();
    for(int i = 0; i < subsets.size(); i++){
      if (sameRankCards(subsets.get(i), allCards)){
        sameRankCards.add(subsets.get(i));
      }
    }

    if(sameRankCards.size() > 0){
      return sameRankCards;
    } else {
      if (handSize % 2 == 0){
        // (handSize/2) then find 2 pairs of cards which are of length at least
        // (handSize/2) if found populate the biggest pair on the left while the
        // smaller number pair will be on the right; sort the array and populate it
        for(int i = 0; i < subsets.size(); i++){
          int[] subset = subsets.get(i);
          Card[] c1 = subsetToCards(subset, allCards);
          Arrays.sort(c1);
          // first we will create 2 differentt array of card type which same size whose length is handSize / 2
          // then we will populate these arrays with the first half and the second half of c1
          // then we check each arrays and wheter they contain the cards of the same rank
          // if both are true add this card to validCardList
          Card[] firstHalf = new Card[handSize / 2];
          Card[] secondHalf = new Card[handSize / 2];

          for(int j = 0; j < (c1.length / 2); j++){
            firstHalf[j] = c1[j];
          }
          for(int j = c1.length / 2; j < c1.length; j++){
            secondHalf[j - (c1.length / 2)] = c1[j];
          }
          if (sameRankCards(firstHalf) && sameRankCards(secondHalf)){
            validSubsets.add(cardsToSubset(c1, allCards));
          }
        }
        // returning even valid subsets
          if(validSubsets.size() > 0){
            return validSubsets;
          }
      } else { // logic for odd
        for(int i = 0; i < subsets.size(); i++) {
          int[] subset = subsets.get(i);
          Card[] c1 = subsetToCards(subset, allCards);
          Arrays.sort(c1);

          int fistRankCards = c1[0].getRank();
          int numCardsForFistRank = 0;
          for(int j = 0; j < c1.length; j++){
            if(fistRankCards == c1[j].getRank()){
              numCardsForFistRank++;
            } else {
              break;
            }
          }
          if ((handSize/2) == numCardsForFistRank || ((handSize/2) + 1) == numCardsForFistRank){
            int secondRankCards = c1[numCardsForFistRank].getRank();
            int numCardsForSecondRank = 0;
            for(int k = numCardsForFistRank; k < c1.length; k++){
              if(secondRankCards == c1[k].getRank()){ //
                numCardsForSecondRank++;
              } else {
                break;
              }
            }
            if (((handSize/2) == numCardsForSecondRank || ((handSize/2) + 1) == numCardsForSecondRank) && numCardsForFistRank != numCardsForSecondRank){
              if(numCardsForFistRank > numCardsForSecondRank){
                validSubsets.add(cardsToSubset(c1, allCards));
              } else {
                Card[] ans = new Card[handSize];
                for(int j = numCardsForFistRank; j < c1.length; j++){
                  ans[j - numCardsForFistRank] = c1[j];
                }
                for(int j = (numCardsForFistRank + 1); j < c1.length; j++){
                  ans[j] = c1[j - numCardsForFistRank - 1];
                }
                validSubsets.add(cardsToSubset(ans, allCards));
              }
            }
          }
        }

        //Is only two types are there or not
      }
    }

    return validSubsets;
  }

//  private Card[] subsetToCards(int[] subsets, Card[] allCards){
//    Card[] cards = new Card[subsets.length];
//    for(int i = 0; i < subsets.length; i++){
//      cards[i] = allCards[subsets[i]];
//    }
//
//    return cards;
//  }




  private boolean sameRankCards(int[] subset, Card[] allCards){
    boolean sameRankCards = true;
    Card card = allCards[subset[0]];
    for(int i = 1; i < subset.length; i++){
      Card c1 = allCards[subset[i]];
      if(card.getRank() != c1.getRank()){
        sameRankCards = false;
        break;
      }
    }
    return sameRankCards;
  }

  private boolean sameRankCards(Card[] cards){
    boolean sameRankCards = true;
    Card card = cards[0];
    for(int i = 1; i < cards.length; i++){
      Card c1 = cards[i];
      if(card.getRank() != c1.getRank()){
        sameRankCards = false;
        break;
      }
    }
    return sameRankCards;
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

    //ArrayList<Card[]> sameRankCards = new ArrayList<>();


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
