package anki;

//import com.google.gson.Gson;
import de.adesso.anki.MessageListener;
import de.adesso.anki.RoadmapScanner;
import de.adesso.anki.Vehicle;
import de.adesso.anki.messages.LocalizationPositionUpdateMessage;
import de.adesso.anki.messages.LocalizationTransitionUpdateMessage;
import de.adesso.anki.messages.SetSpeedMessage;
import de.adesso.anki.roadmap.Roadmap;

public class CustomScanner extends RoadmapScanner {
    Roadmap roadmap;
    Vehicle vehicle;
    MessageListener<LocalizationPositionUpdateMessage> posUpdate;
    MessageListener<LocalizationTransitionUpdateMessage> transUpdate;


    public CustomScanner(Vehicle vehicle) {
        super(vehicle);
        this.vehicle = vehicle;
        roadmap = new Roadmap();
    }

    @Override
    public void startScanning() {
        posUpdate = (message) -> handlePositionUpdate(message);
        vehicle.addMessageListener(
                LocalizationPositionUpdateMessage.class,
                posUpdate
        );
        transUpdate = (message) -> handleTransitionUpdate(message);
        vehicle.addMessageListener(
                LocalizationTransitionUpdateMessage.class,
                transUpdate
        );

        vehicle.sendMessage(new SetSpeedMessage(500, 12500));
    }


    @Override
    public void stopScanning() {
        vehicle.removeMessageListener(LocalizationTransitionUpdateMessage.class, transUpdate);
        vehicle.removeMessageListener(LocalizationPositionUpdateMessage.class, posUpdate);
        vehicle.sendMessage(new SetSpeedMessage(0, 12500));
    }

    /*public String test(){
        String json = new Gson().toJson(super.getRoadmap().toList());
        return json;
    }*/



}
