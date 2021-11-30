package akkaActorFailtureHandling;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;

public class HWApp {

	public static void main(String[] args) {
		ActorRef<String> supervisingActor =
				ActorSystem.create(SupervisingActor.create(), "supervising-actor");
			supervisingActor.tell("failChild");
	}

}
