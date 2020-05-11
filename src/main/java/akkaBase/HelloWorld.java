package akkaBase;

// пакет akka.actor.typed содержит классы ядра akka (это база)
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/*
	Создаем класс HelloWorld, объекты которого обладают способностью
	а) пересылать текстовое сообщение 
	б) принимать текстовое сообщение
	в) изменять текстовое сообщение
	
	Класс наследуется от AbstractBehavior<T>, который типизирован HelloWorld.Command 

    Каждый субъект определяет тип T для сообщений, которые он может получать. 
    Сообщения являются неизменяемыми, поддерживают сопоставление с образцом.
    
    При определении актеров и их сообщений придерживаемся рекомендаций:
    1. Поскольку сообщения являются общедоступным API-интерфейсом Actor, 
       рекомендуется качественно именовать сообщения (чтобы смысл их был ясен),
       даже если они просто "переносят тип данных". 
       Это облегчит использование, понимание и отладку.

    2. Сообщения должны быть неизменными, так как они разделены между различными потоками.

    3. Хорошей практикой является размещение связанных с актером сообщений в виде статических классов 
       в классе AbstractBehaavior. Это облегчает понимание того, какие сообщения ожидает 
       и обрабатывает субъект.

    4. Хорошей практикой является получение исходного поведения актера с помощью статического метода фабрики.
*/

public class HelloWorld extends AbstractBehavior<HelloWorld.Command> {

	private String message = "Hello World!!!"; // Сообщение по умолчанию
	
	/* *************** Определяем команды (начало) *************************** 
	   Интерфейс                                                                                   */
	interface Command{}
	
	//...........   Реализация интерфейса command ..............
	public enum SayHello implements Command{
		INSTANCE
	}
	
	//...........   Реализация интерфейса command ..............
	public static class ChangeMessage implements Command {
       public final String newMessage;

	public ChangeMessage(String newMessage) {
		super();
		this.newMessage = newMessage;
	 }
	}
	// ***************команды (конец) ***************************  
   
	// Конструктор 
	private HelloWorld(ActorContext<Command> context) {
		super(context);
	}
	
	// static фабричный метод, 
	public static Behavior<Command> create(){
		return Behaviors.setup(context -> new HelloWorld(context));
	}
	
		@Override
	public Receive<Command> createReceive(){
		return newReceiveBuilder()
			   .onMessageEquals(SayHello.INSTANCE,this::onSayHello)
			   .onMessage(ChangeMessage.class, this::onChangeMessage)
			   .build();
	}
	
	// Обработчики событий изменения сообщения
	private Behavior<Command> onChangeMessage(ChangeMessage command){
		message = command.newMessage;		
		return this;
	}
	
	// Обработчики событий передачи сообщения
	private Behavior<Command> onSayHello(){
		System.out.println(message);
		return this;
	}
		
}
