import agents.BookBuyerAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class main {

    public static void main(String[] args) {
        Profile myProfile = new ProfileImpl();
        Runtime myRuntime = Runtime.instance();

        ContainerController myContainer = myRuntime.createMainContainer(myProfile);
        try {
            AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
            rma.start();

            String[] books = {"Java for Dummies"};

            AgentController myAgent = myContainer.createNewAgent("book-buyer",
                    BookBuyerAgent.class.getCanonicalName(), books);
            myAgent.start();

        } catch (Exception e) {
            System.out.println("Exception while starting agent" + e.toString());
        }
    }
}
