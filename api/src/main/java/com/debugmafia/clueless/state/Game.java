package com.debugmafia.clueless.state;

import com.debugmafia.clueless.actions.*;
import com.debugmafia.clueless.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.tools.DocumentationTool.Location;

public class Game {
    private Set<Card> winningCards;
    private Board board;
    private Turn turnInstance;

    public Game(Set<Player> players) {
        board = new Board();
        List<Player> playerList = new ArrayList<>(players);
        //TODO: Conversion above needs to have Mrs. Scarlett first
        //TODO: Set the initialPlayers private List to the players
        //TODO: Set the game state to GameState.IN_PROGRESS

        //How to access shuffle deck from board
        board.shuffleDeck();
        winningCards = board.drawWinningCards();
        //TODO: Determine how many cards go to each player (the number will always be a whole number),
        //then call draw() for each player, giving them the cards that were drawn
        
        //TODO: Set the current players turn to the player who will go first, as well as other relevant turn
        //information
    }


    public Game makeMove(Move m) {
        //Restrictions: A piece cannot be moved to a location that does not exist, or to a HALLWAY location that
        //already has a piece in it. Also, the player requesting to move must be equal to the current player.
        board.movePiece(m.getPiece(), m.getTo());
        //TODO: Update the turn state for the current player
            // a. Add the move they just made
            // b. Remove MOVE as an available action for their turn (meaning they cannot make
            // another move)
        return this;
    }

    public Game makeSuggestion(Suggestion s) {
        //Restrictions: The player making the suggestion must also be the one who’s current turn it is. Also, the
        //Location part of the suggestion must equal the location of the current players piece.
        Card suggestedWeapon = board.getAssociatedCard(s.getWeapon());
        Card suggestedLocation = board.getAssociatedCard(s.getRoom());
        Card suggestedPiece = board.getAssociatedCard(s.getPiece());
        //Can we make these into a Set for a suggestionSet?
        
        board.moveWeapons(suggestedWeapon, suggestedLocation);
        board.movePiece(suggestedPiece, suggestedLocation);
        
        //TODO: Iterate over all players, and find the first one that has a card that was a part of the suggestion
        if(playerIterator.hasCard(suggestedWeapon) || playerIterator.hasCard(suggestedLocation) || playerIterator.hasCard(suggestedPiece)){
            //TODO: set player as player to request rebuttal from inside Turn instance
            turnInstance.setRebuttalRequest(playerIterator);
            //TODO: Set the turn state to WAITING_FOR_REBUTTAL
            //TODO: Remove SUGGEST and MOVE as available actions for the current players turn
            //TODO: Set the suggestion in the turn object as the suggestion that was just made
            return this;
        }
        else {
            //TODO: Remove SUGGEST and MOVE as available actions for the current players turn
            //TODO: Set the turn state to IN_PROGRESS
            turnInstance.setSuggestion(/*suggestionSet*/);
            return this;
        }
    }

    public Game makeAccusation(Accusation a) {
        //Restrictions: The player making the accusation must be the player whose current turn it is
        //Is it possible to have these three cards be a list/array/set?
        Card accusedWeapon = board.getAssociatedCard(a.getWeapon());
        Card accusedLocation = board.getAssociatedCard(a.getRoom());
        Card accusedPiece = board.getAssociatedCard(a.getPiece());

        //Can we turn this massive check into a check function?
        //TODO: How to compare suggested cards with winningCards?
        if(accusedWeapon == suggestedWeapon && accusedLocation == suggestedLocation && accusedPlayer == suggestedPiece /* && suggestedCards == winningCards */) {
            this.winner = turnInstance.player;
            //TODO: Set the game state to GameState.COMPLETE
            //TODO: Set the accusation object within the current players turn to this accusation.
            return this;
        }
        else {
            //TODO: Remove this player from the list of active players
            //TODO: Set the accusation object within the current players turn to this accusation.
            //TODO: Set the available actions within the current players turn to END_TURN.
            //TODO: Set the turn state within the current players turn to WAITING_FOR_END_TURN
            return this;
        }
    }

    public Game makeRebuttal(Rebuttal r) {
        //Restrictions: The player making the rebuttal must be the person who was requested a rebuttal from. The card in the rebuttal must be one of the cards 
        //that was made in the original suggestion.
        //TODO: Set the rebuttal object to the current rebuttal on the current players turn
        //TODO: Set the turn state to WAITING_FOR_END_TURN on the current players turn
        //TODO: Set the available actions to only END_TURN on the current players turn
        return this;
    }

    public Game endTurn(Player p) {
        //What is the constructor for Turn object?
        Turn newTurnInstance = new Turn();
        //TODO: Find the next active player to take their turn (next in the array)
        Player nextActivePlayer = this.getNextActivePlayer();
        //TODO: Set that player as the player in the current turn instance
        //How to get current players piece location? Player class and Piece class do not have a getLocation method?
        BoardLocation location = p.getLocation();
        Set<BoardLocation> openAdjacentLocations = board.getOpenAdjacentLocations(location);
        //TODO: Set the available locations property on the current turn object to the result from above
        //TODO: Set this new turn instance to the current players turn
        return this;
    }

    private Player getNextActivePlayer() {
        //TODO: Iterate over the list of active players and grab next active player. If last in list, return first position in list.
        int i = 0;
        while (i < this.activePlayers.size() - 1) {
            if(this.activePlayers.get(i) == this.currentPlayer) {
                if(i < this.activePlayers.size() - 2) {
                    return this.activePlayers.get(i + 1);
                }
                else {
                    return this.activePlayers.get(0);
                }
            }
            else{
                i++;
            }
        }
    }
}
