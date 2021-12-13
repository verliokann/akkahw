package akkaIotSystemModel;

import akka.actor.typed.ActorSystem;

public class IotApplication {

	public static void main(String[] args) {
	    // Create ActorSystem and top level supervisor
		ActorSystem aSystem = ActorSystem.create(IotSupervisor.create(), "iot-system");
		aSystem.terminate();
	  }

}
