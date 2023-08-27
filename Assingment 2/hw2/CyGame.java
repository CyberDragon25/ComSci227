package hw2;
/**
 * Model of a Monopoly-like game. Two players take turns rolling dice to move
 * around a board. The game ends when one of the players has at least
 * MONEY_TO_WIN money or one of the players goes bankrupt (has negative money).
 *
 * @author YOUR_NAME_HERE
 */
public class CyGame {
    /**
     * Do nothing square type.
     */
    public static final int DO_NOTHING = 0;
    /**
     * Pass go square type.
     */
    public static final int PASS_GO = 1;
    /**
     * Cyclone square type.
     */
    public static final int CYCLONE = 2;
    /**
     * Pay the other player square type.
     */
    public static final int PAY_PLAYER = 3;
    /**
     * Get an extra turn square type.
     */
    public static final int EXTRA_TURN = 4;
    /**
     * Jump forward square type.
     */
    public static final int JUMP_FORWARD = 5;
    /**
     * Stuck square type.
     */
    public static final int STUCK = 6;
    /**
     * Points awarded when landing on or passing over go.
     */
    public static final int PASS_GO_PRIZE = 200;
    /**
     * The amount payed to the other player per unit when landing on a
     * PAY_PLAYER square.
     */
    public static final int PAYMENT_PER_UNIT = 20;
    /**
     * The amount of money required to win.
     */
    public static final int MONEY_TO_WIN = 400;
    /**
     * The cost of one unit.
     */
    public static final int UNIT_COST = 50;
    /**
     * Numbers of squares in the game
     */
    private int numOfSquares;
    /**
     * current active player
     */
    private int currentPlayer = 1;
    /**
     * player 1's current square
     */
   private  int player1Square;
    /**
     * player 2's current square
     */
    private int player2Square;
    /**
     * player 1's current money
     */
   private int player1Money;
    /**
     * player 2's current money
     */
   private int player2Money;
    /**
     * player 1's current units
     */
    private int player1units;
    /**
     * player 2's current units
     */
    private int player2units;

// TODO: EVERTHING ELSE
// Note that this code will not compile until you have put in stubs for all
// the required methods.
// The method below is provided for you and you should not modify it.
// The compile errors will go away after you have written stubs for the
// rest of the API methods.
    /**
     * Returns a one-line string representation of the current game state. The
     * format is:
     * <p>
     * <tt>Player 1*: (0, 0, $0) Player 2: (0, 0, $0)</tt>
     * <p>
     * The asterisks next to the player's name indicates which players turn it
     * is. The numbers (0, 0, $0) indicate which square the player is on, how
     * many units the player has, and how much money the player has
     * respectively.
     *
     */

    public CyGame(int numSquares, int startingMoney){
        this.numOfSquares = numSquares;
        player1Money = startingMoney;
        player2Money = startingMoney;
        player1units = 1;
        player2units = 1;
    }

    /**
     *
     * @return one-line string representation of the game state
     */
    public String toString() {
        String fmt = "Player 1%s: (%d, %d, $%d) Player 2%s: (%d, %d, $%d)";
        String player1Turn = "";
        String player2Turn = "";
        if (getCurrentPlayer() == 1) {
            player1Turn = "*";
        } else {
            player2Turn = "*";
        }
        return String.format(fmt,
                player1Turn, getPlayer1Square(), getPlayer1Units(),
                getPlayer1Money(),
                player2Turn, getPlayer2Square(), getPlayer2Units(),
                getPlayer2Money());
    }

    /**
     * Method called to indicate the current player attempts to buy one unit.
     */
    public void buyUnit(){
        int currentPlayer = getCurrentPlayer();
        if (isGameEnded() == false){
            if (currentPlayer == 1 && getSquareType(getPlayer1Square()) == DO_NOTHING && player1Money >= UNIT_COST) {
                player1Money = player1Money - UNIT_COST;
                player1units += 1;
                endTurn();

            } else if (currentPlayer == 2 && getSquareType(getPlayer2Square()) == DO_NOTHING && player2Money >= UNIT_COST) {
                player2Money -= UNIT_COST;
                player2units += 1;
                endTurn();
            }
        }

    }

    /**
     * Method called to indicate the current player passes or completes their turn.
     */
    public void endTurn(){
        if (getCurrentPlayer() == 1){
            currentPlayer = 2;
        } else{
            currentPlayer = 1;
        }
    }

    /**
     *
     * @return Get the current player.
     */
    public int getCurrentPlayer(){

        return currentPlayer;
    }

    /**
     *
     * @return Get Player 1's money.
     */
   public int getPlayer1Money(){

        return player1Money;
    }

    /**
     *
     * @return Get Player 1's square.
     */
    public int getPlayer1Square(){

        return player1Square;
    }

    /**
     *
     * @return Get Player 1's units.
     */
    public int getPlayer1Units(){

        return player1units;
    }

    /**
     *
     * @return Get Player 2's money.
     */
    public int getPlayer2Money(){

        return player2Money;
    }

    /**
     *
     * @return Get Player 2's square.
     */
    public int getPlayer2Square(){

        return player2Square;
    }

    /**
     *
     * @return Get Player 2's units.
     */
    public int getPlayer2Units(){

        return player2units;
    }

    /**
     *
     * @param square
     * @return Get the type of square for the given square number.
     */
    public int getSquareType(int square){
        if (square == 0){
            return PASS_GO;
        } else if (square == 1){
            return  DO_NOTHING;
        } else if (square == numOfSquares - 1) {
            return CYCLONE;
        } else if (square % 2 == 0 && square % 3 != 0 && square % 5 != 0 && square % 7 != 0 && square % 11 != 0){
            return JUMP_FORWARD;
        } else if (square % 5 == 0){
            return PAY_PLAYER;
        } else if (square % 7 == 0 || (square % 11) == 0){
            return EXTRA_TURN;
        } else if (square % 3 == 0){
            return STUCK;
        } else {
            return DO_NOTHING;
        }
    }

    /**
     *
     * @return Returns true if game is over, false otherwise.
     */
   public boolean isGameEnded(){

        if (player1Money >= MONEY_TO_WIN || player1Money < 0 || player2Money >= MONEY_TO_WIN || player2Money < 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param value
     * Method called to indicate the dice has been rolled.
     */
    public void roll(int value){
        int oldSquare = 0;
        if (currentPlayer == 1 && isGameEnded() == false) {
            oldSquare = player1Square;
            if (getSquareType(player1Square) == STUCK){
                if (value % 2 == 0){
                    player1Square = ((player1Square + value) % numOfSquares);
                }
            }
            else {
                player1Square = ((player1Square + value) % numOfSquares);
            }
            if (oldSquare + value >= numOfSquares){
                player1Money += PASS_GO_PRIZE;
            } else if(oldSquare > player1Square) {
                player1Money += PASS_GO_PRIZE;
            } else if (getSquareType(player1Square) == PASS_GO ){
                 player1Money += PASS_GO_PRIZE;
                 endTurn();
             } else if (getSquareType(player1Square) == PAY_PLAYER){
                 player2Money += (player2units * PAYMENT_PER_UNIT);
                 player1Money -= (player2units * PAYMENT_PER_UNIT);
                 endTurn();
             } else if (getSquareType(player1Square) == EXTRA_TURN){

             } else if (getSquareType(player1Square) == STUCK){
                 endTurn();

             } else if (getSquareType(player1Square) == JUMP_FORWARD){
                 player1Square = ((player1Square + 4) % numOfSquares);
                if (player1Square + 4 >= numOfSquares){
                    player1Money += PASS_GO_PRIZE;
                }

                 endTurn();

             } else if (getSquareType(player1Square) == DO_NOTHING){
                 endTurn();
             } else if (getSquareType(player1Square) == CYCLONE){
                 player1Square = player2Square;
                 endTurn();
             }
        } else if (currentPlayer == 2 && isGameEnded() == false) {
            oldSquare = player2Square;
            if (getSquareType(player2Square) == STUCK){
                if (value % 2 == 0){
                    player2Square = ((player2Square + value) % numOfSquares);
                }
            }
            else {
                //oldSquare = player1Square;
                player2Square = ((player2Square + value) % numOfSquares);
            }
            if (oldSquare + value >= numOfSquares){
                player2Money += PASS_GO_PRIZE;
            } else if(oldSquare > player2Square) {
                player2Money += PASS_GO_PRIZE;
            } else if (getSquareType(player2Square) == PASS_GO ){
                player2Money += PASS_GO_PRIZE;
                endTurn();
            } else if (getSquareType(player2Square) == PAY_PLAYER){
                player1Money += (player1units * PAYMENT_PER_UNIT);
                player2Money -= (player1units * PAYMENT_PER_UNIT);
                endTurn();
            } else if (getSquareType(player2Square) == EXTRA_TURN){

            } else if (getSquareType(player2Square) == STUCK){
                endTurn();

            } else if (getSquareType(player2Square) == JUMP_FORWARD){
                player2Square = ((player2Square + 4) % numOfSquares);
                if (player2Square + 4 >= numOfSquares){
                    player2Money += PASS_GO_PRIZE;
                }

                endTurn();

            } else if (getSquareType(player2Square) == DO_NOTHING){
                endTurn();
            } else if (getSquareType(player2Square) == CYCLONE){
                player2Square = player1Square;
                endTurn();
            }
    }
}

    /**
     *  Method called to indicate the current player attempts to sell one unit.
     */
    public void sellUnit(){
        int currentPlayer = getCurrentPlayer();
        if (isGameEnded() == false){
            if (currentPlayer == 1 && player1units > 0) {
                player1Money = player1Money + UNIT_COST;
                player1units -= 1;
                endTurn();
            } else if (currentPlayer == 2 && player2units > 0){
                player2Money += UNIT_COST;
                player2units -= 1;
                endTurn();
            }
        }

    }

}
