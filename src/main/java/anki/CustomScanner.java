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
    JSONObject locationIDs;
    JSONObject reversedPieces;
    static int counter;
    SocketController controller;

    public CustomScanner(Vehicle vehicle, SocketController controller) {
        super(vehicle);
        this.vehicle = vehicle;
        this.controller = controller;
    }

    @Override
    public void startScanning() {
        locationIDs = new JSONObject();
        reversedPieces = new JSONObject();
        counter = 0;
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
            locationIDs.put(String.valueOf(counter), lastPosition.getRoadPieceId());
            System.out.println(lastPosition.isParsedReverse());
            reversedPieces.put(String.valueOf(counter), lastPosition.isParsedReverse());
            counter++;
            //idList+=""+lastPosition.getLocationId()+":";
            if (super.roadmap.isComplete()) {
                this.stopScanning();
                //System.out.println("STOPPING CAUSE I'M RETARDED LELELELELELE");
            }
        }
    }

    @Override
    public void handlePositionUpdate(LocalizationPositionUpdateMessage message) {
        lastPosition = message;
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

        sendMap();

    }

    public JSONArray test(){
        JSONArray jsonToSend = new JSONArray();

        List<Roadpiece> current = super.roadmap.toList();

        for(Roadpiece r: current){
            jsonToSend.put(r);
        }

        return jsonToSend;
    }

    public JSONObject getTypeList(){
        JSONObject typeList = new JSONObject();
        List<Roadpiece> pieces = super.roadmap.toList();
        //boolean test = true;
        for(int i = 0; i < pieces.size(); i++){
            typeList.put(String.valueOf(i), pieces.get(i).getType());
            System.out.println(typeList.getString(String.valueOf(i)));
        }
        return typeList;
    }

    /*public JSONObject getIdList(){
        JSONObject idlist = new JSONObject();
        List<Roadpiece> pieces = super.roadmap.toList();
        for(int i = 0; i < pieces.size(); i++){
            System.out.println(idlist.getString(String.valueOf(i)));
        }
        return idlist;
    }*/


    public void sendMap(){
        JSONObject object = new JSONObject();
        JSONObject data = new JSONObject();
        object.put("event", "roadmap");
        data.put("map", getTypeList());
        data.put("ids", locationIDs);
        data.put("reversed", reversedPieces);
        data.put("length", getRoadmap().toList().size());
        object.put("data", data);
        controller.getClient().send(object.toString());
        System.out.println("map sent---------------------------------------------------------------------------------------");
    }



}
