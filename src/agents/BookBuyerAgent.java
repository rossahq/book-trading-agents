package agents;

import jade.core.AID;
import jade.core.Agent;

public class BookBuyerAgent extends Agent{
    String nickname = "Peter";
    AID id = new AID(nickname, AID.ISLOCALNAME);
    private String targetBookTitle;
    private AID[] sellerAgents = { new AID("seller1", AID.ISLOCALNAME),
                                    new AID("seller2", AID.ISLOCALNAME)};
    protected void setup() {
        System.out.println("Hello! Buyer-agent " + getAID().getName()+ " is ready");

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            targetBookTitle = (String) args[0];
            System.out.println("I want to buy " + targetBookTitle);
        } else {
            System.out.println("No book title specified");
            doDelete();
        }
    }

    protected void takeDown() {
        System.out.println("Buyer-agent " + getAID().getName() + " terminating");
    }
}
