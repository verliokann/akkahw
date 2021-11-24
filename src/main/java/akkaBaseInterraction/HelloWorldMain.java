package akkaBaseInterraction;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class HelloWorldMain extends AbstractBehavior<HelloWorldMain.SayHello> {

	  // Секция описания сообщений --------------------------------------------
	  public static class SayHello {
	    public final String name;

	    public SayHello(String name) {
	      this.name = name;
	    }
	  }

	  // (конец) Секция описания сообщений --------------------------------------------
	  
	  
	  // Фабричный метод для создания актора HelloWorldMain ----------------------------------------------
	  public static Behavior<SayHello> create() {
	    return Behaviors.setup(HelloWorldMain::new);
	  }

	  private final ActorRef<HelloWorld.Greet> greeter;

	  //------------------------------------------------------------------------------
	  //Конструктор
	  private HelloWorldMain(ActorContext<SayHello> context) {
	    super(context);
	    greeter = context.spawn(HelloWorld.create(), "greeter");
	  }

	  @Override
	  public Receive<SayHello> createReceive() {
	    return newReceiveBuilder().onMessage(SayHello.class, this::onStart).build();
	  }

	  private Behavior<SayHello> onStart(SayHello command) {
	    ActorRef<HelloWorld.Greeted> replyTo = getContext().spawn(HelloWorldBot.create(10), command.name);
	    greeter.tell(new HelloWorld.Greet(command.name, replyTo));
	    return this;
	  }

	  //-------------------------------------------------------------------
   	  public static void main(String[] args) {
			final ActorSystem<HelloWorldMain.SayHello> system =
				    ActorSystem.create(HelloWorldMain.create(), "hello");

				system.tell(new HelloWorldMain.SayHello("World"));
				system.tell(new HelloWorldMain.SayHello("Akka"));
				
				system.terminate();
	  }	
}