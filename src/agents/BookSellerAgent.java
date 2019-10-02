package agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Hashtable;

public class BookSellerAgent extends Agent {

    private Hashtable catalogue;

    private BookSellerGui myGui;

    protected void setup() {
        catalogue = new Hashtable();

        myGui = new BookSellerGui(this);
        myGui.show();

        addBehaviour(new OfferRequestServer());
        addBehaviour(new PurchaseOrderServer());
    }

    protected void takeDown() {
        myGui.dispose();
        System.out.println("Seller-agent " + getAID().getName() + " terminating");
    }

    /**This is invoked by the GUI when the user adds a new book for sale*/
    public void updateCatalogue(final String title, final int price) {
        addBehaviour(new OneShotBehaviour() {
        public void action() {
            catalogue.put(title, new Integer(price));
            }
        } );
    }

    private class PurchaseOrderServer extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt =
                    MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();
                Integer price = (Integer) catalogue.remove(title);
                if (price != null) {
                    reply.setPerformative(ACLMessage.INFORM);
                    System.out.println(title + " sold to agent " + msg.getSender().getName());
                }
                else {
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            }
            else {
                block();
            }
        }
    }

    private class OfferRequestServer extends Behaviour {
        @Override
        public void action() {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                String title =  msg.getContent();
                ACLMessage reply = msg.createReply();

                Integer price = (Integer) catalogue.get(title);
                if (price != null) {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(String.valueOf(price.intValue()));
                }
                else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            }
        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
