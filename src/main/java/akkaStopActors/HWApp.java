package akkaStopActors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;

public class HWApp {
public static void main(String[] args) {
	ActorRef<String> first = ActorSystem.create(StartStopActor1.create(), "first");
	first.tell("stop");
}	
}
