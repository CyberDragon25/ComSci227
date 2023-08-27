package hw4;
import api.Card;
import api.IEvaluator;

/**
 * The class AbstractEvaluator includes common code for all evaluator types.
 * 
 * TODO: Expand this comment with an explanation of how your class hierarchy
 * is organized.
 */
public abstract class AbstractEvaluator implements IEvaluator {

    protected Card[] subsetToCards(int[] subsets, Card[] allCards){
        Card[] cards = new Card[subsets.length];
        for(int i = 0; i < subsets.length; i++){
            cards[i] = allCards[subsets[i]];
        }

        return cards;
    }

    protected int[] cardsToSubset(Card[] cards, Card[] allcards){
        int[] subsets = new int[cards.length];

        for(int i = 0; i < cards.length; i++){
            for(int j = 0; j < allcards.length; j++){
                if(cards[i] == allcards[j]){
                    subsets[i] = j;
                }
            }
        }
        return subsets;
    }

    protected Card[] cardsMatchingSubsets(Card[] allcards, int[] subset){
        Card[] subsetCards = new Card[subset.length];
        for(int i = 0; i < subset.length; i++){
            subsetCards[i] = allcards[subset[i]];
        }
        return subsetCards;
    }

}
