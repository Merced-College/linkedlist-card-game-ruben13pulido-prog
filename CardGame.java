//package linkedLists;
// Ruben Pulido, Jason Loa
// CPSC - 39
// 11/25/2025
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Scanner;

// Blackjack game implemented using LinkedList for the deck and hands.
// Demonstrates file loading, shuffling, drawing cards, and game logic.

public class CardGame {

    public static void main(String[] args) {
        // Scanner for user input in the console 
        Scanner in = new Scanner(System.in);
        System.out.println("Simple Blackjack - LinkedList Version");
        // Main loop that repeats until the player decides to stop
        boolean keepPlaying = true;
        while (keepPlaying) {
            List<Card> deck;
            try {
                // Load the deck of cards from cards.txt (stored in a LinkedList)
                deck = loadDeck("cards.txt");
            } catch (IOException e) {
                System.out.println("Failed to load deck: " + e.getMessage());
                return;
            }
            // Shuffle the deck before playing each round 
            Collections.shuffle(deck);
            // Player and dealer hands stored as LinkedLists 
            LinkedList<Card> player = new LinkedList<>();
            LinkedList<Card> dealer = new LinkedList<>();
           // Iniital deal: two cards each 
            player.add(draw(deck));
            dealer.add(draw(deck));
            player.add(draw(deck));
            dealer.add(draw(deck));

            System.out.println();
            // Dealer shows only one card; the other is hidden 
            System.out.println("Dealer shows: " + cardShort(dealer.get(0)) + " and [hidden]");
            // Display player's full hand and total value
            System.out.println("Your hand: " + handShort(player) + "  (" + handValue(player) + ")");
            
            // Check for natural blackjack (21 with two cards)
            boolean playerBlackjack = (handValue(player) == 21 && player.size() == 2);
            boolean dealerBlackjack = (handValue(dealer) == 21 && dealer.size() == 2);
           // Immediate result if someone has blackjack
            if (playerBlackjack || dealerBlackjack) {
                System.out.println("Dealer hand: " + handShort(dealer) + "  (" + handValue(dealer) + ")");
                if (playerBlackjack && dealerBlackjack) {
                    System.out.println("Push: both have Blackjack.");
                } else if (playerBlackjack) {
                    System.out.println("You have Blackjack! You win!");
                } else {
                    System.out.println("Dealer has Blackjack. You lose.");
                }
            } else {
                // Player turn: hit or stand
                boolean playerBusted = false;
                while (true) {
                    System.out.print("Hit or Stand? (h/s): ");
                    String resp = in.nextLine().trim().toLowerCase();
                    if (resp.equals("h") || resp.equals("hit")) {
                        // Draw a card from the top of the deck (removeFirst from LinkedList)
                        Card c = draw(deck);
                        player.add(c);
                        System.out.println("You draw: " + cardShort(c));
                        int pv = handValue(player);
                        System.out.println("Your hand: " + handShort(player) + "  (" + pv + ")");
                        // Bust condition
                        if (pv > 21) {
                            System.out.println("You busted!");
                            playerBusted = true;
                            break;
                        } else if (pv == 21) {
                            break;
                        }
                    } else if (resp.equals("s") || resp.equals("stand")) {
                        break;
                    } else {
                        System.out.println("Please enter 'h' or 's'.");
                    }
                }
                // Dealer turn only happens if player didn't bust 
                if (!playerBusted) {
                    System.out.println();
                    // Dealer reveals hidden card
                    System.out.println("Dealer reveals: " + cardShort(dealer.get(0)) + ", " + cardShort(dealer.get(1)) + "  (" + handValue(dealer) + ")");
                   // Dealer must hit until total is at least 17
                    while (handValue(dealer) < 17) {
                        Card c = draw(deck);
                        dealer.add(c);
                        System.out.println("Dealer draws: " + cardShort(c) + "  (" + handValue(dealer) + ")");
                    }

                    int pv = handValue(player);
                    int dv = handValue(dealer);
                   // Final comparison 
                    if (dv > 21) {
                        System.out.println("Dealer busted. You win!");
                    } else if (dv > pv) {
                        System.out.println("Dealer wins (" + dv + " vs " + pv + ").");
                    } else if (dv < pv) {
                        System.out.println("You win (" + pv + " vs " + dv + ").");
                    } else {
                        System.out.println("Push (" + pv + ").");
                    }
                }
            }
              // Ask player to play again
            System.out.print("Play again? (y/n): ");
            String again = in.nextLine().trim().toLowerCase();
            if (!again.equals("y") && !again.equals("yes")) {
                keepPlaying = false;
            }

            System.out.println();
        }

        System.out.println("Thanks for playing.");
        in.close();
    }
    // Loads deck information from a text file into a LinkedList
    // Each line format: suit, name, value, optional picture 
    private static List<Card> loadDeck(String filename) throws IOException {
        List<Card> deck = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String suit = parts[0].trim();
                String name = parts[1].trim();
                int value;
                try {
                    value = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    value = 0;
                }
                String pic = (parts.length >= 4) ? parts[3].trim() : "";

                deck.add(new Card(suit, name, value, pic));
            }
        }
        return deck;
    }

    private static Card draw(List<Card> deck) {
        if (deck.isEmpty()) return null;
        return ((LinkedList<Card>) deck).removeFirst();
    }

    private static int handValue(List<Card> hand) {
        int sum = 0;
        int aces = 0;
        for (Card c : hand) {
            int v = c.getCardValue();
            if (v == 11) aces++;
            sum += v;
        }
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }
        return sum;
    }

    private static String cardShort(Card c) {
        if (c == null) return "[none]";
        return c.getCardName() + " of " + c.getCardSuit();
    }

    private static String handShort(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(cardShort(hand.get(i)));
        }
        return sb.toString();
    }
}