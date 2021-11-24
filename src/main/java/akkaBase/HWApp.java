package akkaBase;

import akka.actor.typed.ActorSystem;

public class HWApp {

	public static void main(String[] args) {
		
		ActorSystem<HelloWorld.Command> mySystem = ActorSystem.create(HelloWorld.create(), "MySystem"); // Создаем систему акторов 
		
		mySystem.tell(HelloWorld.SayHello.INSTANCE);
		mySystem.tell(HelloWorld.SayHello.STARINSTANCE);
		
		mySystem.tell(new HelloWorld.ChangeMessage("HelloActorWorld"));
		
		mySystem.tell(HelloWorld.SayHello.INSTANCE);
		mySystem.tell(HelloWorld.SayHello.STARINSTANCE);	

		mySystem.terminate();
		
		
	}
}