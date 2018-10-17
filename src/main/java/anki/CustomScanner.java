package anki;

//import com.google.gson.Gson;
import org.json.*;
import de.adesso.anki.MessageListener;
import de.adesso.anki.RoadmapScanner;
import de.adesso.anki.Vehicle;
import de.adesso.anki.messages.LocalizationPositionUpdateMessage;
import de.adesso.anki.messages.LocalizationTransitionUpdateMessage;
import de.adesso.anki.messages.SetSpeedMessage;
import de.adesso.anki.roadmap.Roadmap;
import de.adesso.anki.roadmap.roadpieces.Roadpiece;

import java.util.List;

public class CustomScanner extends RoadmapScanner {
    Vehicle vehicle;
    MessageListener<LocalizationPositionUpdateMessage> posUpdate;
    MessageListener<LocalizationTransitionUpdateMessage> transUpdate;


    public CustomScanner(Vehicle vehicle) {
        super(vehicle);
        this.vehicle = vehicle;
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
    protected void handleTransitionUpdate(LocalizationTransitionUpdateMessage message) {
        if (lastPosition != null) {

            super.roadmap.add(
                    lastPosition.getRoadPieceId(),
                    lastPosition.getLocationId(),
                    lastPosition.isParsedReverse()

            );
            //idList+=""+lastPosition.getLocationId()+":";
            if (super.roadmap.isComplete()) {
                this.stopScanning();
                System.out.println("STOPPING CAUSE I'M RETARDED LELELELELELE");
            }
        }
    }

    @Override
    public void stopScanning() {

        vehicle.sendMessage(new SetSpeedMessage(0, 12500));
        vehicle.removeMessageListener(LocalizationTransitionUpdateMessage.class, transUpdate);
        vehicle.removeMessageListener(LocalizationPositionUpdateMessage.class, posUpdate);



        /*JSONObject object = new JSONObject();
        JSONObject data = new JSONObject();

        object.put("event", "roadmap");
        data.put("map", test());
        object.put("data", data);

        SocketController.getClient().send(object.toString());*/

    }

    public JSONArray test(){
        JSONArray jsonToSend = new JSONArray();

        List<Roadpiece> current = super.roadmap.toList();

        for(Roadpiece r: current){
            jsonToSend.put(r);
        }

        return jsonToSend;
    }

    public String getIdList(){
        String idlist = "";
        List<Roadpiece> pieces = super.roadmap.toList();
        for(Roadpiece r: pieces){
            idlist+=""+r.getType() + ":";
            System.out.println(r.getType());
        }
        return idlist;
    }


    public void sendMap(){
        JSONObject object = new JSONObject();
        JSONObject data = new JSONObject();
        object.put("event", "roadmap");
        data.put("map", getRoadmap());
        object.put("data", data);
        SocketController.getClient().send(object.toString());
    }



}
