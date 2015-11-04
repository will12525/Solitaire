package solitare;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Solitaire extends Canvas {

    //card dimensions 73, 106
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private boolean running = false;
    private static boolean clicked = false;

    private boolean firstClick = true;
    private int fpsS = 0;
    private JFrame frame;
    private long timeStart = 0;
    private long deleteM = 0;

    private Deck deck;

    private List<Card> pileInHand = new ArrayList<>();

    private List<Card> deckFlipped = new ArrayList<>();

    private Map<Pile, List<Card>> stacks = new HashMap<>();

    private List<String> message = new ArrayList<>();

    private int lastPile = 0;
    private int score = 0;

    public Solitaire() {
        frame = new JFrame("Solitaire by William Lawrence");
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new Mouse());
        setIgnoreRepaint(true);

        frame.setVisible(true);

        running = true;
        runGame();
    }


    public void init() {

        deck = new Deck(1);


        populatePlaces();


        deleteM = System.currentTimeMillis();
    }

    public void tick() {
        if (message.size() > 0 || message.size() > 10) {
            if (System.currentTimeMillis() - deleteM > 1000 * 20) {
                message.remove(0);
                deleteM = System.currentTimeMillis();
            }

        }
        boolean cardInHand = false;
        boolean checkWin = false;
        boolean placeCard = false;
        int mouseX;
        int mouseY;
        if (clicked) {
            if (firstClick) {
                timeStart = System.currentTimeMillis();
                firstClick = false;
            }
            if (pileInHand.size() > 0) {
                cardInHand = true;
            } else {
                pileInHand.clear();
                cardInHand = false;
            }
            mouseY = Mouse.getY();
            mouseX = Mouse.getX();

            int xOffset = frame.getWidth() / 10;
            int yOffset = frame.getHeight() / 5;

            //place cards
            if (cardInHand) {

                for (Pile p : Pile.values()) {
                    List<Card> cards = stacks.get(p);
                    int size = cards.size() - 1;
                    if (mouseY > yOffset && !p.isFinal()) {
                        //placing a card on the stacks
                        if (cards.size() > 0) {
                            if (((cards.get(size).getX() * xOffset) < mouseX) && (((cards.get(size).getX() + 1) * xOffset) > mouseX)) {
                                if (pileInHand.size() > 0) {
                                    System.out.println(cards.get(size).getSuite() + ", " + pileInHand.get(0).getSuite() + ", " + cards.get(size).getValue() + 1 + ", " + pileInHand.get(0).getValue());
                                    if (placeCard(pileInHand.get(0), cards.get(size))) {
                                        placeCard = place(p,cards);

                                        if(lastPile==14)
                                        {
                                            score+=5;
                                        }
                                        else if(lastPile>10)
                                        {
                                            score -=15;
                                        }
                                    }
                                }
                            }
                        } else {
                            if (cards.size() == 0) {
                                //placing a king on an empty spot
                                if (pileInHand.size() > 0&&pileInHand.get(0).getValue()==13) {
                                    if (placeCard(pileInHand.get(0), null)) {
                                       placeCard = place(p,cards);

                                    }
                                }
                            }
                        }
                    } else if (p.isFinal() && mouseY < yOffset && pileInHand.size() <= 1) {
                        //placing card on final stack
                        if ((((cards.get(0).getX() - 4) * xOffset) < mouseX) && (((cards.get(0).getX() - 3) * xOffset) > mouseX)) {
                            System.out.println(cards.get(0).getSuite() + ", " + pileInHand.get(0).getSuite() + ", " + cards.get(size).getValue() + 1 + ", " + pileInHand.get(0).getValue());
                            if (cards.get(0).getSuite().equals(pileInHand.get(0).getSuite()) && cards.get(size).getValue() + 1 == pileInHand.get(0).getValue()) {
                                placeCard = place(p,cards);
                                checkWin=true;
                                score+=10;
                            }
                        }


                    }
                }
                //puts cards back
                if (!placeCard) {
                    if (lastPile == 14) {
                        deckFlipped.add(pileInHand.get(0));
                        pileInHand.clear();
                    } else {
                        boolean didntPlace = true;
                        Pile possiblePlace = null;
                        for(Pile p : Pile.values())
                        {
                            List<Card> stack = stacks.get(p);
                            if(stack.size()>0)
                            {
                                if(stack.get(0).getX()==lastPile)
                                {
                                    didntPlace = !place(p,stack);
                                    break;
                                }
                            }
                            else
                            {
                                possiblePlace = p;
                            }
                        }
                        if(didntPlace)
                        {
                            place(possiblePlace,stacks.get(possiblePlace));
                        }
                    }
                }
                else if(checkWin)
                {
                    int count = 0;
                    for(Pile p : Pile.values())
                    {
                        List<Card> stack = stacks.get(p);
                        if(stack.size()>0) {
                            if (stack.get(0).getX() >= 8) {
                                if(stack.get(stack.size()-1).getValue()==13)
                                {
                                    count++;
                                }
                            }
                        }
                    }
                    if(count==4)
                    {
                        message.clear();
                        message.add("You win!");
                    }
                }
            }
            //pickup cards
            if (!cardInHand && pileInHand.size() <= 0) {
                List<Card> cardsToRemove = new ArrayList<>();
                //the piles
                for (Pile p : Pile.values()) {
                    if (mouseY > yOffset && !p.isFinal()) {
                        List<Card> stack = stacks.get(p);
                        boolean pickUp = false;
                        if (stack.size() > 0) {
                            for (int k = 0; k < stack.size(); k++) {
                                Card card = stack.get(k);
                                if ((mouseX > xOffset * card.getX()) && (mouseX < xOffset * (card.getX() + 1))) {

                                    if(k+1==stack.size())
                                    {
                                        if (card.getFlipped()) {
                                            pileInHand.add(card);
                                            cardsToRemove.add(card);
                                            lastPile = card.getX();
                                            pickUp = true;
                                        } else if (k == stack.size() - 1) {
                                            card.flip();
                                            score+=5;
                                        }
                                        System.out.println("hi");
                                    }
                                    else if (card.getY() > mouseY) {
                                        if (card.getFlipped()) {
                                            pileInHand.add(card);
                                            cardsToRemove.add(card);
                                            lastPile = card.getX();
                                            pickUp = true;
                                        } else if (k == stack.size() - 1) {
                                            card.flip();
                                            score+=5;
                                        }
                                        System.out.println("hello");
                                    }
                                }
                            }
                        }
                        //runs if a card was picked up, cleans up the stack cards were taken from
                        if (pickUp) {
                            stack.removeAll(cardsToRemove);
                            stacks.put(p, stack);
                            cardsToRemove.clear();
                        }
                    }
                    //the final piles
                    if (mouseY < yOffset && p.isFinal()) {
                        List<Card> stack = stacks.get(p);
                        if (stack.size() > 1) {
                            if (((xOffset * (stack.get(0).getX() - 3)) > mouseX) && ((xOffset * (stack.get(0).getX() - 4)) < mouseX)) {
                                Card card = stack.get(stack.size() - 1);
                                pileInHand.add(card);
                                stack.remove(card);
                                stacks.put(p, stack);
                                lastPile = card.getX();

                            }
                        }
                    }

                }

                //the deck
                if (mouseY < yOffset) {
                    if (mouseX < xOffset * 2 && mouseX > xOffset) {
                        if (deck.getSize() > 0) {
                            deckFlipped.add(deck.getCard(deck.getSize() - 1));
                            deckFlipped.get(deckFlipped.size() - 1).flip();
                            deck.removeCard(deck.getSize() - 1);
                        } else if (deckFlipped.size() > 0) {
                            //puts all cards back in the deck
                            for (int x = deckFlipped.size() - 1; x >= 0; x--) {
                                Card temp = deckFlipped.get(x);
                                temp.flip();
                                deck.add(temp);
                                score-=100;
                            }
                            deckFlipped.clear();
                        }
                    }
                    //the flipped deck
                    if (mouseX < xOffset * 3 && mouseX > xOffset * 2) {
                        if (deckFlipped.size() > 0) {
                            pileInHand.add(deckFlipped.get(deckFlipped.size() - 1));
                            deckFlipped.remove(pileInHand.get(0));
                            lastPile = 14;
                        }
                    }
                }
            }
            //flip cards
            if(pileInHand.size()==0) {
                for (Pile p : Pile.values()) {
                    List<Card> stack = stacks.get(p);
                    if (stack.size() > 0) {
                        if (!(p.isFinal()) && !(stack.get(stack.size() - 1).getFlipped())) {
                            stack.get(stack.size() - 1).flip();
                        }
                    }
                }
            }
        }
        if(score<0)
        {
            score = 0;
        }
        clicked = false;



    }

    public void render() {
        Point point = MouseInfo.getPointerInfo().getLocation();

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        int xOffset = frame.getWidth() / 10;
        int yOffset = frame.getHeight() / 5;

        int xOff = 3;


        for (Pile p : Pile.values()) {
            if (p.isFinal()) {
                List<Card> finalPile = stacks.get(p);
                int fPos = xOff - 4;
                for (int pos = 0; pos < finalPile.size(); pos++) {
                    g.drawImage(finalPile.get(pos).getCard(), xOffset * fPos, 0, null);
                    stacks.get(p).get(pos).setY(0);
                    stacks.get(p).get(pos).setX(xOff);
                }
            } else {
                List<Card> pile = stacks.get(p);
                int yPos = 30;
                if(pile.size()>10) {
                    yPos = 20;
                }
                for (int pos = 0; pos < pile.size(); pos++) {
                    if (pile.get(pos).getFlipped()) {
                        g.drawImage(pile.get(pos).getCard(), xOffset * xOff, yOffset + (yPos * pos), null);
                    } else {
                        g.drawImage(deck.getHolder("back").getCard(), xOffset * xOff, yOffset + (yPos * pos), null);
                    }
                    stacks.get(p).get(pos).setY(yOffset + (yPos * (pos + 1)));
                    stacks.get(p).get(pos).setX(xOff);
                }
            }
            xOff++;
        }

        if (deckFlipped.size() > 0) {
            g.drawImage(deckFlipped.get(deckFlipped.size() - 1).getCard(), xOffset * 2, 0, null);
        }
        if (deck.getSize() > 0) {
            g.drawImage(deck.getHolder("back").getCard(), xOffset, 0, null);
        }

        if (pileInHand.size() > 0) {
            for (int x = 0; x < pileInHand.size(); x++) {
                g.drawImage(pileInHand.get(x).getCard(), (int) (point.getX() - frame.getLocation().getX() - (deck.getHolder("back").getCard().getWidth() / 2)), (int) ((point.getY() + (x * 20)) - frame.getLocation().getY()), null);
            }
        }

        g.setColor(Color.WHITE);
        g.drawString("FPS: " + fpsS, 20, 20);
        g.drawString("Score: " + score, 20, 40);
        if (timeStart != 0) {
            g.drawString("Time: " + (System.currentTimeMillis() - timeStart) / 1000, 20, 60);
        } else {
            g.drawString("Time: " + 0, 20, 60);
        }
        //prints the debug messages
        g.setColor(Color.white);
        for (int x = 0; x < message.size(); x++) {
            g.drawString(message.get(x), 20, ((20 * x) + 400));
        }
        g.dispose();
        bs.show();
    }

    public boolean place(Pile p,List<Card> cards)
    {
        for (Card c : pileInHand) {
            cards.add(c);
        }

        pileInHand.clear();
        stacks.put(p, cards);
        return true;
    }

    public boolean placeCard(Card cardToPlace, Card placeOn) {
        if (placeOn == null) {
            if (cardToPlace.getValue() == 13) {
                message.add("true");

                return true;
            }
            message.add("false");

            return false;
        }
        if (((cardToPlace.getValue() + 1) == placeOn.getValue()) && (cardToPlace.getColor() != placeOn.getColor())) {
            message.add("true");

            return true;
        }
        message.add("false");
        return false;
    }

    public void populatePlaces() {
        int pos = 0;
        for (Pile p : Pile.values()) {
            stacks.put(p, new ArrayList<>());
            if (!p.isFinal()) {
                List<Card> stack = stacks.get(p);
                populate(stack, p.getStackSize());
            } else {
                List<Card> finalStack = stacks.get(p);
                switch (pos) {
                    case 7:
                        finalStack.add(deck.getHolder("spade"));
                        break;
                    case 8:
                        finalStack.add(deck.getHolder("club"));
                        break;
                    case 9:
                        finalStack.add(deck.getHolder("diamond"));
                        break;
                    case 10:
                        finalStack.add(deck.getHolder("heart"));
                        break;
                }
            }
            pos++;
        }
    }

    public void populate(List<Card> aPile, int size) {
        for (int x = 0; x < size; x++) {
            int cardToMove = (int) (Math.random() * deck.getSize());
            Card card = deck.getCard(cardToMove);
            if (x + 1 == size) {
                card.flip();
            }
            aPile.add(card);
            deck.removeCard(cardToMove);
        }
    }

    public void runGame() {
        long lastLoopTime = System.nanoTime();
        double nanoTick = 1000000000 / 60;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        init();


        while (running) {
            long now = System.nanoTime();
            delta += (now - lastLoopTime) / nanoTick;
            lastLoopTime = now;
            while (delta >= 1) {

                ticks++;

                tick();

                delta -= 1;

            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            frames++;

            render();


            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println("Ticks: " + ticks + ", frames: " + frames);
                fpsS = frames;
                frames = 0;
                ticks = 0;
            }

        }
    }

    public static void toClip(String m) {
        StringSelection selection = new StringSelection(m);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        System.exit(0);
    }

    public static void setClicked() {
        clicked = true;
    }

    public static void main(String[] args) {

        new Solitaire();
    }

}
