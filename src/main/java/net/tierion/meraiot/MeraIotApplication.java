package net.tierion.meraiot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;

public class MeraIotApplication {
	static final String BROKER_URI = "tcp://broker.hivemq.com:1883";
	static final String CLIENT_ID = "TIERION-MERA-IOT-DATA-CLIENT";
	static final String INPUT_TOPIC = "iot-hub-kzn-bloody-raw";
	static final String OUTPUT_TOPIC = "iot-hub-kzn-bloody-result";

	public static void main(String[] args) throws MqttException {
		MqttClient client = new MqttClient(BROKER_URI, CLIENT_ID, new MemoryPersistence());
		client.setCallback(new MqttCallback() {
			private ObjectMapper mapper = new ObjectMapper();
			private BeaconService beaconService = new BeaconService();

			@Override
			public void connectionLost(Throwable throwable) {

			}

			@Override
			public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
				if (!INPUT_TOPIC.equals(s)) {
					return;
				}
				String json = new String(mqttMessage.getPayload());
				System.out.println(json);
				try {
					List<BeaconDTO> data = mapper.readValue(json, new TypeReference<List<BeaconDTO>>(){});
					PositionDTO positionDTO = beaconService.calculatePosition(data);
					client.publish(OUTPUT_TOPIC,
							mapper.writeValueAsString(positionDTO).getBytes(),
							2,
							true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

			}
		});
		client.connect();
		client.subscribe(INPUT_TOPIC);
		while (true) {}
		// очень плохо, что client.disconnect() не делается
	}
}
