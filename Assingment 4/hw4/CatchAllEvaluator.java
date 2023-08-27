package hw4;

import api.Card;
import api.Hand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Evaluator satisfied by any set of cards. The number of
 * required cards is equal to the hand size.
 * 
 * The name of this evaluator is "Catch All".
 */
//Note: You must edit this declaration to extend AbstractEvaluator
//or to extend some other class that extends AbstractEvaluator
public class CatchAllEvaluator extends AbstractEvaluator
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
  public CatchAllEvaluator(int ranking, int handSize)
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
    return "Catch All";
  }

  @Override
  public int cardsRequired() {
    return handSize();
  }

  @Override
  public boolean canSatisfy(Card[] mainCards) {
    if(mainCards.length == handSize){
      return true;
    }
    return false;
  }

  @Override
  public boolean canSubsetSatisfy(Card[] allCards) {
    Card[] sortedCards = allCards;
    if(allCards.length >= cardsRequired()){
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Hand createHand(Card[] allCards, int[] subset) {
    Card[] mainArr = new Card[handSize()];
    for(int i = 0; i < handSize(); i++){
      mainArr[i] =allCards[i];
    }
    Arrays.sort(mainArr);
    boolean valid = canSatisfy(mainArr);
    if(!valid){
      return null;
    }
    Hand hand  = new Hand(mainArr, null, this);
    return hand;
  }

  @Override
  public Hand getBestHand(Card[] allCards) {
    Hand h = createHand(allCards, null);
    return h;
  }
}
