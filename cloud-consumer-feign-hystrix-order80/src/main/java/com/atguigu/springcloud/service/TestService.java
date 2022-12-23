package com.atguigu.springcloud.service;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.http.annotation.Contract;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
//import sun.misc.SharedSecrets;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
//import sun.util.resources.LocaleData;

import javax.annotation.Resource;
import javax.el.MethodNotFoundException;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Null;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Thread.sleep;
import static java.lang.Thread.yield;

import static java.lang.Thread.*;//includes all methods/variables import


class TestJDBC{
    public static void main(String[] args) throws ClassNotFoundException {
        //yield();
        //int h=MIN_PRIORITY;
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Object driver = BeanUtils.instantiateClass(clazz);
        ds.setDriver((java.sql.Driver) driver);
        ds.setUrl("jdbc:mysql://localhost:3306/mtravel_db?verifyServerCertificate=false&autoReconnect=true&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("630716");
        JdbcTemplate jdbcTemplate=new JdbcTemplate(ds);

        String orginalamount = jdbcTemplate.queryForObject(
                "SELECT SYSDOCID FROM etrvrequests WHERE TRAVELID = ?",
                String.class, "5FA03AD3-6558-E511-80BF-000C29B0C763");
        System.out.println(orginalamount);
    }
}


//inheritance example 1:
@FunctionalInterface
interface Sphere {//all are implicitly public, and interface method not allow final
    public final static int a=1;///interface variable are default public static final

    public static void fly(){//since jdk 1.8 static method in interface, 1.7 not work
        //a=2;
    }
    public abstract void flyHigh() throws FileNotFoundException;
    public  default  String getName() { return "Unknown"; }
    public  default  String getName1() { return "Unknown"; }
    //public  default  boolean equals(Object o) { return true; }
    //default cannot override object equals(Object o)
}
abstract class Planet {
    int gl;
    static final int ng;//must be initiated in static block
    static {
        ng=1;
        Sphere.fly();
    }
    {
        //ng=1;
    }
    public Planet(){
        //ng=1;
    }
    public int getG() throws Exception{
        return gl;
        //return 1;//cannot have 2 return in one function-> unreachable statement
    }

    abstract String getName();
}
class Mars extends Planet implements Sphere{
    public Mars() { super(); }

    @Override
    public synchronized void flyHigh() {//throws FileNotFoundException //optional

    }
    @Override
    public synchronized int getG() throws RuntimeException{
        synchronized(this){
            return gl;
            //return 1;//cannot have 2 return in one function-> unreachable statement
        }
    }
    public int talk(String... x) {
        System.out.println("talk x constructor");
        return x.length;
    }
    public int talk() {
        System.out.println("talk empty constructor");
        return 1;
    }
    //if RuntimeException change to a checked Exception
    // like Exception,IOException,FileNotFoundException etc.-> compilation error
    public String getName() throws RuntimeException { return "Mars"; }
    //polymorphism in java allows us to perform the same action in many different ways.
    public static void main(final String[] probe) //throws ArithmeticException
    {
        //int g=1/0;
        if (probe!=null && probe.length>0) {
            System.out.println(probe[0]);
        }
        Mars mars = new Mars();
        mars.talk();
        System.out.print(((Planet)mars).getName());//Mars
        //**Object's parent can hold the object and run the code with
        //its own implementation
    }
}

//inheritenace example 2: initialization block
abstract class Car {
    int no=0;
    static { System.out.print("1"); }
    public Car(String name) {
        super();
        System.out.print("2");
    }
    { System.out.print("3"); } }
class BlueCar extends Car {
    int no=1;
    { System.out.print("4"); }
    public BlueCar() {
        super("blue");//**this super run b4 this class initialization block
        System.out.print("5");
    }
    public static void main(String[] gears) {
        new BlueCar();//13245
    }
}


abstract class Ball {
    protected final int size;
    public Ball(int size) { this.size = size; }
}
interface Equipment {}
class SoccerBall extends Ball implements Equipment {
    public SoccerBall() {
        super(5);
    }

    public Ball get() {
        return this;
    }

    public static void main(String[] passes) {
        Equipment equipment = (Equipment) (Ball) new SoccerBall().get();
        System.out.print(((SoccerBall) equipment).size);
    }
}

//inheritance example 3: override static
abstract class Parallelogram {
    private int getEqualSides() {return 0;}
}
abstract class Rectangle extends Parallelogram {
    public static int getEqualSides() {return 2;} // x1
}
final class Square extends Rectangle {
    //instance method cannot override static method
    //public int getEqualSides() {return 4;} // x2

    public static void main(String[] corners) {
        final Square myFigure = new Square(); // x3
        System.out.print(myFigure.getEqualSides());
    }
}

//inheritance eg 4: abstract
abstract class Rotorcraft {
    protected final int height = 5;
    static  Number talk(){return 0;}
    Number see(){return 0;}
    abstract int fly();
}
//final abstract class RotorcraftF { abstract class cannot be final, final class cannot be inherited from
//    protected final int height = 5;
//    abstract int fly();
//}
class Helicopter extends Rotorcraft  {
    private int height = 10;

    //compile error:
    //instance method in child-class cannot override static method in parent class
    //static method in child-class cannot override instance method in parent class
    static Integer talk(){return 1;}//method hiding, ** static replace static is not called override
    @Override
    Integer see(){return 1;}//override

    protected int fly() { return super.height; }
    public static void main(String[] unused) {
        Helicopter h = (Helicopter)new Helicopter();//return 5
        System.out.print(h.fly()+" - "+h.height);//5 - 10
    }
}

//***inheritance eg 5: default method in interface - diamond issue
interface SpeakDialogue {
    public final static int h=1;//public final static  is default for interface variable
    public static void canCall(){//added in jdk 1.8 interface static method

    }
    public int walk();
    default int talk() { return 7; }
}
interface SingMonologue {
    public int walk();
    default int talk() { return 5; }
//    default boolean equals(Object o){//note default method cannot override parent method
//        return true;
//    }
}
class Performance implements SpeakDialogue, SingMonologue {
    static void canCall(){

    }
    public int walk(){return 1;}//must be public, if not compile time error
    public int talk() { return 1; }// if without this line, compile time error
    //class implements multiple interfaces containing same default method must implement default method
    //bcz java does not support multiple inheritance bcz of diamond issue(extend 2 class having same method, the child does not know to call use which in runtime,
    // so it only supports multiple interface inheritance bcz of interface method are not concrete,
    // but since jdk1.8, default method was introduced which is concrete so the same diamond issue raised up again
    // to solve this, the child class who implements multiple interfaces having same default method must
    // override and define the default method to prevent the diamond issue)
    public int talk(String... x) { return x.length; }
    public static void main(String[] notes)
    {
        SpeakDialogue.canCall();
        System.out.print(new Performance().talk(notes));//0
        System.out.print(new Performance().talk());//1
    }
}

//***inheritance eg 6. hiding variable in subclass
class Math {
//    Math(){
//        super();
//    }
    //Math(int a){}
    public double secret = 2;
    void calculate(){
        System.out.println(++secret);
    }
}
class ComplexMath extends Math {
//    public ComplexMath(){
//        //super(5);
//    }
    public double secret = 4;
    void calculate(){
        System.out.println("ComplexMath===>"+(++secret));
    }
}
class InfiniteMath extends ComplexMath {
    public double secret = 8;
//    void calculate(){
//        System.out.println("InfiniteMath===>"+(++secret));
//    }
    //note that polymorphism only apply to method but not variable
    public static void main(String[] numbers) {
        Math math = new InfiniteMath();
        System.out.println(math.secret); //2.0
        ComplexMath omplexMath = new InfiniteMath();
        System.out.println(omplexMath.secret); //4.0
        omplexMath.calculate();//5.0

        InfiniteMath infiniteMath = new InfiniteMath();
        System.out.println(infiniteMath.secret); //8.0
        infiniteMath.calculate();//5.0
    }
}

//***inheritance eg 7. passing subclass as param in method
class Canine {}
class Dog extends Canine {}
class Wolf extends Canine {}
final class Husky extends Dog {}
class Zoologist {
    Canine animal;
    public final void setAnimal(Dog animal) {
        this.animal=new Wolf();
        this.animal=new Dog();
        this.animal=new Canine();

        this.animal = animal;
    }
    public void calc(long p){
        //calcc(p);//cannot pass long to int, need explicit cast
    }
    public void calcc(int p){
        calc(p);//can pass int to long, implicit cast
    }
    public static void main(String[] furryFriends) {
        //new Zoologist().setAnimal(new Wolf()); //invalid

        Wolf wolf = new Wolf();wolf.hashCode();

        new Zoologist().setAnimal(new Husky());
        new Zoologist().setAnimal(null);
    }
}

//inheritance eg 8. abstract method
abstract class Bear {
    public Bear(int j){
        System.out.println("Bear abstract class constructor is allowed:"+j);
    }
    protected int sing;
    protected abstract int grunt();
    int sing() {
        return sing;
    }
}
class PolarBear extends Bear {
    public PolarBear(){
        super(1);
    }
    protected int grunt() { //override cannot have less accessible modifier
        int g=-1;
         //g+= 10;
         g+=sing();//instance var init to 0
        //sing()+=1;//not allow
        //return super.grunt()+1; //cannot access abstract method directly
        return g;
    }

    public static void main(String[] args) {
        Integer _123=123;
        int[] as=new int[2];
        System.out.println(as instanceof Object);
        System.out.println(_123.hashCode());

        PolarBear polarBear = new PolarBear();
        System.out.println(polarBear.sing());
        System.out.println(polarBear.grunt());

        System.out.println(null instanceof PolarBear);//false
        System.out.println(polarBear instanceof PolarBear);//true
    }
}



//OCA Chapter 8: Handling Exceptions
// Handling Exceptions eg 1. method throws exception need wrap by try catch or throws
class Fortress {
    //if method throws exception
    public void openDrawbridge() throws Throwable { // p1
        try {
            throw new Exception("Circle");

        }
        catch (Throwable e)
        {
            System.out.print("Opening!");
        }
        finally {
            System.out.print("Walls"); // p2
        }
    }
    public static void main(String[] moat)  {
        try {
            //if not surround openDrawbridge with try catch cannot compile
            new Fortress().openDrawbridge(); // p3
        } catch (Throwable e) {
            e.printStackTrace();
        }

        System.out.println("====================");
        try {System.out.print('A');
            throw new RuntimeException("Out of bounds!");
        } catch (ArrayIndexOutOfBoundsException t) {
            System.out.print('B'); throw t;
        } finally { System.out.print('C'); }





    }
}

//Handling Exception eg 2. Checked vs Unchecked Exception handling
class CastleUnderSiegeException extends Exception {}
class KnightAttackingException extends CastleUnderSiegeException {}
class Citadel {
    //RuntimeException is unchecked exception
    //CastleUnderSiegeException is checked exception
    public void openDrawbridge() throws RuntimeException { // q1
        try {
            throw new KnightAttackingException();
        }
        catch (Exception e) {

            throw new ClassCastException();
        } finally {
            try {
                throw new CastleUnderSiegeException(); // q2
            } catch (CastleUnderSiegeException e) {
                e.printStackTrace();
            }
            //throw new CastleUnderSiegeException(); // q2
            // //CastleUnderSiegeException is checked exception
            //so it must be handled here
        }
    }
    public static void main(String[] moat) {
        new Citadel().openDrawbridge(); // q3
        //note that openDrawbridge() declares unchecked exception RuntimeException
        //so it does not need to handle in try catch in main method
    }

}

//Handling Exception eg 3. Checked Exception Handling
class Computer {
    public void compute() throws Exception {
        throw new RuntimeException("Error processing request");
    }
    public static void main(String[] bits) {
        try {
            new Computer().compute();
            //must be handled with proper type compatible checked exception
            System.out.print("Ping");
        } catch (NullPointerException e) { //NullPointerException does not cover checked Exception
            System.out.print("Pong"); throw e;
        }
        catch (Exception e) {//comment this catch to expose  compute checked exception again
            System.out.print("Pong"); //throw e;//if here throws e, it will still consider not handled
        }
    }
}

//Handling Exception eg 4. try catch sequence with return
class Stranger {
    public static String getFullName(String firstName, String lastName) {
        try {
            return firstName.toString() + " " + lastName.toString();
            //finally execute after this return
        }
        catch (NullPointerException npe) {
            System.out.print("Problem?");
        }finally {
            System.out.print("Finished!");
            //return "finally";
        }
        return null;
    }
    public static String testStringRe(){
        StringBuilder sb=new StringBuilder();
        try{
            sb.append("a");
            //sb.delete()
            return sb.append("b").toString();
        }finally {
            sb=null;
        }
    }
    public static void getName(String firstName, String lastName) {
        try {
            System.out.print(firstName.toString() + " " + lastName.toString());
            return;//note that even if it returns here, finally still execute

        }finally {
            System.out.println("getName finally!");
            return;
        }
        //unreachable line
        //System.out.println("getName Finished!");
        //return;
    }
    public static void main(String[] things) {
        System.out.println(testStringRe());
        getName("Joyce","Hopper");//Joyce HoppergetName Finished!
        System.out.print(getFullName("Joyce","Hopper"));//Finished!Joyce Hopper

        final Exception exception = new Exception();
        final Exception data = (ClassCastException)exception;//ClassCastException
        //ClassCastException & IllegalArgumentException are subclass of RuntimeException
        System.out.print(data);

    }
}


//Handling Exception  eg 5. checked exception override method
class Organ {
    public void operate() throws Exception
    {
        throw new RuntimeException("Not supported");
    }
}
class Heart extends Organ {
    //overriden method declared checked exception cannot be new or broader exception from
    //parent method
    public void operate() throws Exception {//throws Exception
        System.out.print("beat");
    }
    public static void main(String... cholesterol) throws Throwable {//note here can use broader class
        new Heart().operate();
//        try {
//            new Heart().operate();
//        }catch(Error e){
//            e.printStackTrace();
//        } finally {
//
//        }
    }
}


class Coat {
    public Long zipper() throws Exception {
        try {
            String checkZipper = (String)new Object();
        } catch (Exception e) {
            throw new RuntimeException("Broken!");
        }
        return null;
    }
    public static void main(String... warmth) {
        try {
            new Coat().zipper();
            System.out.print("Finished!");
        } catch (Throwable t) {}
    }
}

//Handling Exception eg 6. interface declaring checked exception
class OutOfBoundsException extends BadCatchException {}
class BadCatchException extends Exception {}
interface Outfielder {
    public void catchBall() throws OutOfBoundsException;
}
class OutfielderIml implements Outfielder{

    @Override
    public void catchBall() throws OutOfBoundsException {

    }
}


//OCA Chapter 9 Selected Java class API
//Selected Java class API eg. 1 StringBuilder test obj ref
class SelectedJavaClassApi{

    public static void main(String[] args) {
        StringBuilder line = new StringBuilder("-");

        StringBuffer buf = new StringBuffer("-");
        //initial capacity = pass in len + 16
        System.out.println("init capacity:"+line.capacity());//17
        StringBuilder anotherLine = line.append("-");//stringBuilder append return the same obj rather than create new
        System.out.print(line == anotherLine);//true
        System.out.print(" ");
        System.out.println(line.length());//2
        System.out.print(line.capacity());//17
        // int newCapacity = (value.length << 1) + 2;

        //predicate
        java.util.function.Predicate<StringBuilder>
                p =(StringBuilder b) -> {return true;};

        p =(StringBuilder b) ->  true;//same as the line above
        //p =StringBuilder b ->  true;//failed to compile
        p =(b) ->  true;//same as the line above
        p =b ->  true;//same as the line above

        //list
        List<Character> chars = new ArrayList<>();
        chars.add('a'); chars.add('b');
        chars.set(1, 'c');
        chars.remove(0);//this remove index cannot > list size or else will throw indexOutOfBoundsException
        System.out.print(chars.size() + " " + chars.contains('b'));

        //if use  Arrays.asList , cannot alter the list, orelse throw runtime exception
        //Exception in thread "main" java.lang.UnsupportedOperationException
        String[] array = {"Natural History", "Science", "Art"};
//        List<String> museums = new ArrayList<>();
//        for (String s : array) {
//            museums.add(s);
//        }

        Integer.valueOf(1);//return Integer wraper object
        //char cha=Integer.parseInt("2");//not allow//return primitives int
        char cha1=2;//allow
        List<String> museums = Arrays.asList(array);//return a final array
        museums.remove(2);
        System.out.println(museums);

        //String
        String b = "12";
        b += "3";
        //b.reverse();//String has no reverse() method, it is in StringBuilder
        System.out.println(b.toString());



    }
}
class Shoot {
    //SAM Single Abstract Method
    @FunctionalInterface
    interface Target {
        boolean needToAim(double angle);
        //boolean needToAima(double angle);
    }
    static void prepare(double angle, Target t) {
        boolean ready = t.needToAim(angle); // k1
        System.out.println(ready);
    }
    public static void main(String[] args) {

        String[][] naf= new String[5][2];
        naf[0] = new String[5];
        naf[1] = new String[15];//its valid even if declared array has only 2 in 2nd dimension
        naf[1][14]="ok";
        //naf[1][15]="ok";//runtimeException
        //naf[10] = new String[10];
        System.out.println("passed string array:"+Arrays.toString(naf));
        for (String[] strings : naf) {
            System.out.println("passed string inner array:"+Arrays.toString(strings));
        }


        List li= new ArrayList();
        List li2= new ArrayList<Integer>();
        li.add("k");li.add(1);
        li2.add(200.9d);li2.add("ok");
        for(int h=li2.size();--h>=0;){
            Object o = li2.get(h);
            if(o instanceof String){
                System.out.println("is string:"+(String)o);
            }
            if(o instanceof Integer){
                System.out.println("is Integer:"+(Integer)o);
            }
            if(o instanceof Float){
                System.out.println("is Float:"+(Float)o);
            }
            if(o instanceof Double){
                System.out.println("is Double:"+(Double)o);
            }
        }
        System.out.println("test list"+li);
        System.out.println("test list2"+li2);


        //java.util.function.Predicate dash = c -> c.startsWith(" ");
// bcz Predicate has no explicit type, default will be Object, so Object has no startsWith -> compile error
//        System.out.println(dash.test("–"));

        java.util.function.Predicate<String> dash = c -> c.startsWith("-");
        System.out.println(dash.test(" "));//false
        System.out.println(dash.test("-="));//true
        System.out.println(dash.and(c -> c.startsWith("-")));//java.util.function.Predicate$$Lambda$3/1579572132@123a439b

        prepare(45, d -> d > 5 || d < -5); // k2


        //java.time date are immutable

        java.time.LocalDateTime ldt= java.time.LocalDateTime.now();
        System.out.println(ldt);//2022-01-19T21:24:17.227
        java.time.LocalTime lt= java.time.LocalTime.now();
        System.out.println(lt);//21:24:17.227
        System.out.println("LocalDateTime getDayOfMonth->"+ldt.getDayOfMonth());//19
        System.out.println("LocalDateTime getMonth->"+ldt.getMonth().getValue());//1
        java.time.LocalDate ld= java.time.LocalDate.now();//2022-01-19

        System.out.println("LocalDate getMonth->"+ld.getMonth().getValue());//1
        System.out.println("LocalDate getMonth->"+ld.getMonth());//JANUARY




        int[] ints = new int[1];
        System.out.println(ints.length);

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.size();


        LocalDate newYears = LocalDate.of(2017, 1, 1);
        //newYears.plusMonths()//will return new LocalDate
        //note LocalDate is immutable, no setter method like setYear(), etc.
        java.time.LocalDateTime newYearst = java.time.LocalDateTime.of(2017, 1, 1,0,0,0);
        java.time.LocalDateTime newYearst1 = java.time.LocalDateTime.of(2017, 1, 1,1,0,0);
        java.time.Period period = java.time.Period.ofDays(1);//care abt zone
        //**Period : Day, Month, Week, Year only, no time hh:mm:ss available,  periods are for larger units of time
        //**Duration is measured in smaller unit like seconds, getSeconds(), durations are for smaller units of time
        java.time.Duration duration = java.time.Duration.ofDays(1);//does not care zone
        //ChronoUnit.MINUTES -> Enum range from  ChronoUnit.NANOS ~ ChronoUnit.FOREVER
        //ChronoUnit.NANOS -> Enum
        //ChronoUnit.CENTURIES -> 100years
        //ChronoUnit.ERAS -> 1 billion year

        //ChronoUnit.FOREVER -> Long.MAX_VALUE year
        //Duration toString for 1 day: PT24H (Hour,Minute,Second)
        //Period toString for 1 day: P1D (Year,Month,Day)
        //*** if use duration , cannot add to LocalDate, bcz LocalDate has no seconds/nanoseconds part
        //so it will throw [java.time.temporal.UnsupportedTemporalTypeException: Unsupported unit: Seconds]
        //A Duration measures an amount of time using time-based values (seconds, nanoseconds).
        // A Period uses date-based values (years, months, days). here is the link
        java.time.format.DateTimeFormatter formatDateTime = java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss");
        java.time.format.DateTimeFormatter formatDate = java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy");
        System.out.println(formatDate.format(newYears.minus(period)));
        //note for LocalDate cannot use hh:mm:ss else error throws:
        //[java.time.temporal.UnsupportedTemporalTypeException: Unsupported field: ClockHourOfAmPm]
        System.out.println(formatDateTime.format(newYearst.minus(duration)));

//        System.out.println("new Date() => "+new Date());//Sat Aug 13 21:02:52 SGT 2022
        System.out.println("LocalDate.now() => "+java.time.LocalDateTime.now());//2022-08-13T21:05:11.766
        SimpleDateFormat sdf =new SimpleDateFormat("MM-dd-yyyy");
        System.out.println("SimpleDateFormat=> "+sdf.format(new Date()));
//        for (Locale availableLocale : Locale.getAvailableLocales()) {
//            System.out.println(availableLocale);
//        }

        //chapter 17: Use JAVA SE 8 Date/Time API
//        java.time.ZonedDateTime zdt =new java.time.ZonedDateTime();
//        ZoneOffset zo =new

        //ChronoUnit
        java.time.Instant previous, current;

        previous = java.time.Instant.now().minusMillis(1000);
        current = java.time.Instant.now();
        java.time.LocalDateTime dtnow=java.time.LocalDateTime.now();
        System.out.println("ChronoUnit previous instance dtnow :"+dtnow);//2022-06-06T17:42:01.617
        System.out.println("ChronoUnit previous instance temporal :"+previous);//2022-06-06T09:42:00.617Z
        System.out.println("ChronoUnit current instance temporal :"+current);//2022-02-02T16:09:53.857Z -> GMT ZULU Time
        long gap = java.time.temporal.ChronoUnit.MILLIS.between(previous,current);
        System.out.println("ChronoUnit gap btw temporal :"+gap);
        long gap1 = ChronoUnit.MINUTES.between(newYearst,newYearst1);
        System.out.println("ChronoUnit gap1 btw temporal :"+gap1);
        //newYearst,newYearst1


        //ZoneId
        LocalDate trainDay = LocalDate.of(2017, 5, 13);
        java.time.LocalTime time = java.time.LocalTime.of(10, 0);
        java.time.ZoneId zone = java.time.ZoneId.of("America/Los_Angeles");
        java.time.ZonedDateTime zdt = java.time.ZonedDateTime.of(trainDay, time, zone);
        System.out.println("ZoneId zdt: "+zdt);//2017-05-13T10:00-07:00[America/Los_Angeles]
        //newYearst.toInstant()//need  a ZoneOffset
        java.time.Instant instantz = zdt.toInstant();//Instant convert to GMT zulu time
        instantz = instantz.plus(1, ChronoUnit.DAYS);
        System.out.println("ZoneId instantz: "+instantz);//2017-05-14T17:00:00Z

        //change LocalDate test
        LocalDate datea = LocalDate.of(2017, java.time.Month.JULY, 17);
        datea.isAfter(ld);
        java.time.LocalTime timea = java.time.LocalTime.of(10, 0);
        java.time.ZoneId zonea = java.time.ZoneId.of("America/New_York");
        java.time.ZonedDateTime iceCreamDay = java.time.ZonedDateTime.of(datea, timea, zonea);
        datea = datea.plusMonths(1);//does not change ZonedDateTime iceCreamDay
        System.out.println("change LocalDate test: "+iceCreamDay.getMonthValue());

        ////not exists -> LocalDate now = new LocalDate();
        //// exists -> LocalDate now =  LocalDate.now();

        //string
        System.out.println("\n\n\n=========================String============================\n");
        String builder = "54321";
        builder = builder.substring(4);
        System.out.println(builder);

        String ng="abcdef";
        ng = ng.substring(0,3);
        CharSequence cs=new CharSequence() {
            @Override
            public  int length() {
                return 0;
            }

            @Override
            public char charAt(int index) {
                return 0;
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return null;
            }
        };
        //ng.replace()

        System.out.println(ng);//abc

        //System.out.println(builder.charAt(1));//java.lang.StringIndexOutOfBoundsException

        //StringBuilder
        StringBuilder sb = new StringBuilder("abc");
        StringBuffer sbu = new StringBuffer("abc");
        ng=ng.replace(sbu,"hello");//string replace can pass in CharSequence, since String & StringBuilder are both charSequence
        System.out.println("ng ========> "+ng);//hello

        List alist = new ArrayList();//can compile but with warning, generic(can hold any type)
        alist.add("as");alist.add(1);alist.add(new Object());
        System.out.println("alist ===> "+alist);
        ng.toUpperCase();
        sb.append("red");//abcred
        sb.deleteCharAt(0);//bcred //might throw java.lang.StringIndexOutOfBoundsException
        sb.delete(1, 2); //might throw java.lang.StringIndexOutOfBoundsException if start index not in range
        System.out.println(sb);//bred


        //List
        List<Integer> pennies = new ArrayList<>();
        pennies.add(3); pennies.add(2); pennies.add(1);
        Integer integer = new Integer(2);
        pennies.remove(integer);pennies.remove(integer);
        //remove compare the object value

        //list remove can remove by index or remove by object
        System.out.println(pennies);



    }
}

class Cowboy {
    private int space = 5;
    private double ship = space < 2 ? 1 : 10; // g1
    public void printMessage() {
        if(ship>1) {
            System.out.println("Goodbye");
        }
        if(ship<10 && space>=2)
            System.out.println("Hello"); // g2
        else
            System.out.println("See you again");
    }
    public static final void main(String... stars) {
        new Cowboy().printMessage();
    }
}

//Casting
//class String{
//    public String(String s){
//        System.out.println(s);
//    }
//}
class Tool {
    void use() { } // r1
 }
 class Hammer extends Tool {
    private void use(String str) { } // r2
     public void bang() { } // r3

}

class Building {}
class House extends Building{
    public static void main(String... args) {
        Building b = new Building();
        House h = new House();
        Building bh = new House();
        //Building p = (House) b; //ClassCastException will be thrown at runtime
        //House q = (Building) h; //cannot compile, parent cannot assign to child
        //House q = bh;//parent cannot assign to child
        Building rp = h;//child can assign to parent
        Building r = (Building) bh;
        House s = (House) bh;
        System.out.println("blocka b4");
        blocka :{

            //if(s==bh) break blocka;
            System.out.println("blocka");
            if(s instanceof Collection && s instanceof Serializable
            && s instanceof ExecutorService  && s instanceof Executor){
                //instanceof interface always able to compile, but instanceof class cannot
                System.out.println("House is an instance of any interface is able to compile : eg. Collection & Serializable & ExecutorService & Executor");
            }

        }
        System.out.println("blocka after");

    }

}

//OCP advanced api
//enum example
 enum EnumAnimal {
    CAT {
         public  String makeNoise() { return "MEOW!"; }
    },
     DOG {
        public String makeNoise() { return "WOOF!"; }
    };

    abstract String makeNoise();
}
abstract class ProviderAnim {
    protected EnumAnimal c = EnumAnimal.CAT;
    static void callg(){}
     abstract void kok();
}
class Vacation {
    //enum is default static
    static void enjoy(){
        class RaceCar{//inner class is allowed
            //static void okv(){}
            void drive(){
                System.out.println("enjoy driving ");
            }
        }
        final RaceCar raceCar = new RaceCar();
        raceCar.drive();
    }
    private static  enum  DaysOff implements Serializable {//enum cannot be abstract

        Thanksgiving{
            public void walk(){

            }
        },
        PresidentsDay{
            void walk(){

            }
        },
        ValentinesDay {
            void walk(){

            }
        };

        private DaysOff(){//enum constructor default and is compulsory private
            System.out.println("enum constructor DaysOff invoked");
        }//enum constructor must be private and is default private
        abstract void walk();//if enum has abstract method, then the inner child must implement it
        void drive(){

        }
    }//enum cannot be final
    static int Integer;
    int Integer_instance;
    public static void main(String... unused) {

        int Integer=12;
        int Integer_instance=13;//
        System.out.println("Integer ->"+Integer);
        System.out.println("Integer_instance ->"+Integer_instance);


        //DaysOff da= new DaysOff();
        class RaceCar{

            {
                System.out.println("RaceCar initializer 2");
            }
            void drive(){
                System.out.println("main driving ");
            }
            {
                System.out.println("RaceCar initializer 1");
            }
        }
        enjoy();
        final RaceCar raceCar = new RaceCar();
        raceCar.drive();

        System.out.println("=====> "+DaysOff.valueOf("Thanksgiving").name());//=====> Thanksgiving
        //System.out.println("=====> "+DaysOff.valueOf("ak"));
        // it throw java.lang.IllegalArgumentException "No enum constant"

        for (DaysOff value : DaysOff.values()) {
            System.out.println(value);//string
        }

        final DaysOff input = DaysOff.Thanksgiving;

        final int ordinal = DaysOff.Thanksgiving.ordinal();
        int gf;
//        if(gf>0){//local var cannot be used without given a value
//        }
                System.out.println("================ switch test begging");
        //**switch case(no break) execution order
        // if has matching case(if no matched one, then default is the match),
        // it will start from the matched one until the end[follow declared sequence] (include default)
        // ***if no matched one(no default), will not go in any
        switch(input) {//switch if without break, even value not match it will also execute


            //default: System.out.println("default"); //break;//default is not compulsory to provide
            case ValentinesDay: System.out.println("1"); //break;
            //
            //case Thanksgiving: System.out.println("99=>"); //break;
            case PresidentsDay: System.out.println("2"); //break;
            //case Thanksgiving: System.out.println("99=>"); //break;
            //case DaysOff.PresidentsDay: System.out.print("2"); //break; //this line is wrong
        }


        System.out.println(1+ 1==2?"a":"b");
        System.out.println("================");

//        String chair, table = "metal";
//        chair = chair + table;
//        System.out.println(chair);
        //double num1, int num2 = 0;
        float f1=1.1f;//by default is double
        double d1=1.1f;
        int i1=1;
        long l1=100;
        f1=i1;
        d1=i1;
        d1=f1;
        l1=i1;
        d1=l1;
        Integer iwr=new Integer(i1);
        Double dwr=new Double(i1);//convert double to int
        i1=dwr.intValue();
        //d1=i1;

        int ggi;
        {
            if(0==Integer){
                //System.out.println(ggi);//variable "ggi" might not have been initialized
            }
        }

        double Double = iwr.doubleValue();


        String fnals ="a";
        switch(fnals) {//switch if without break, even value not match it will also execute
            default:
                int gg=10;
                System.out.println("fnals default");
                break;
            case "b":
                gg=100;
                System.out.println("fnals b"+gg); break;
            case "c": System.out.println("fnals c"); break;
        }

        //while(1==1);
        System.out.println("All comparison with NaN returns false ======>");
        System.out.println(10>Float.NaN);
        System.out.println(10>=Float.NaN);
        System.out.println(10<=Float.NaN);
        System.out.println(10==Float.NaN);
        System.out.println(Float.NaN==Float.NaN);


        System.out.println("divide by 0 with floating point ======>");
        System.out.println(10.0/0);//Infinity
        //System.out.println(0/0);//java.lang.ArithmeticException: / by zero
        System.out.println(0/0.0);//NaN
        System.out.println(0/0.1);//0.0
        System.out.println(Float.POSITIVE_INFINITY/Float.POSITIVE_INFINITY);//NaN
        System.out.println(Float.POSITIVE_INFINITY/0);//Infinity
        System.out.println(Float.POSITIVE_INFINITY/1);//Infinity
    }
}


//inner class
class Matrix {
    private int level = 1;
    class Deep {
        private int level = 2;
        class Deeper {
            private int level = 5;
            public void printReality() {
                System.out.print(level); //5
                System.out.print(" "+Matrix.Deep.this.level);//2
                System.out.print(" "+Deeper.this.level);//5
                System.out.print(" "+this.level);//5
                System.out.print(" "+Matrix.this.level);//1
            }
        }
    }
    static class VC{}

    public static void main(String[] bots) {
        new Matrix.VC();
        Matrix.Deep.Deeper simulation = new Matrix().new Deep().new Deeper();
        simulation.printReality();



        class RaceCar  {
            public String makeNoise() {
                return null;
            };
        };
        RaceCar r =new RaceCar();


        //local variables referenced from a lambda expression must be final or effectively final
        //A variable or parameter whose value is never changed after it is initialized is effectively final.
    }
}

//inner static class
class Bottle {
    public static class Ship {
        private enum Sail { // w1
            TALL {protected int getHeight() {return 100;}},
            SHORT {protected int getHeight() {return 2;}};
            protected abstract int getHeight();
            //Sail(int i){}
        }
        public Sail getSail() {
            return Sail.TALL;
        }
    }
    public static void main(String[] stars) {
        Bottle bottle = new Bottle();
        //Ship q = bottle.new Ship(); // w2 -> qualified new of static Class
        Ship q1 =  new Bottle.Ship();
        System.out.print(q1.getSail());
    }
}

//inner class effectively final access
 class Woods {
    static class Tree {

        private Tree(int h){

        }
        private Tree(){

        }
    }
    public static void main(String[] leaves) {
        int water = 10+5;//effectively final
        final class Oak extends Tree { // p1
            //water=2;
            private Oak(int j){
                //super(j);
            }
            public Oak(){
                //super(j);
            }
            public int getWater() {
               // water=2;//variable accessed from inner class must be final/effectively final
                return water; // p2
            }
        }
        System.out.print(new Oak().getWater());
    }
}


//inner class member
 class Penguin {
    private int volume = 1;
    private class Chick {
        //private static int volume = 3;
        private int volume = 3;
        void chick() {
            System.out.print("Honk("+Penguin.Chick.this.volume+")!");//3
            System.out.print("Honk("+Penguin.this.volume+")!");//1
        }
    }
    public static void main(String... eggs)
    {
        Penguin pen = new Penguin();
        final Penguin.Chick littleOne = pen.new Chick();
        littleOne.chick();
    }
}


//override interface static method
interface Alex {
    public default void write() {}
    public static void publish() {}
    public abstract void think();
}
interface Michael {
    public default  void write() {}
    public static void publish() {}
    public void think();
}
class Twins implements Alex, Michael {
    @Override public void write() {}

    //@Override //Override here will cause compile error
    public static void publish() {}
    //override apply to instance member only, not static class member

    @Override
    public void think() {
        System.out.print("Thinking...");
    }
}

//enum param
 enum Proposition {
    TRUE(10) {
        @Override
        protected String getNickName() { return "RIGHT"; }
    },
    FALSE(-10) {
        public String getNickName() { return "WRONG"; }
    },
    UNKNOWN(0) {// Proposition.FALSE.getValue();
        @Override
        public synchronized String getNickName() { return "LOST"; }

        @Override
        public  int getValue() { return 1; }
    };
    private final int value;
    static void method(){
        return;
    }
    //enum constructor are assumed private if no access modifier is specified
    //enum constructor can only be private
    //class constructor are assumed package private default
    Proposition(int value) {

        System.out.println("enum Proposition val:"+value);
        this.value = value;
    }
    public synchronized int getValue() {
        return this.value;
    }
    protected abstract  String getNickName();

    public static void main(String[] args) {
        Proposition.FALSE.name();
        Proposition.TRUE.name();
        Proposition.UNKNOWN.name();
        int val = Proposition.FALSE.value;
        System.out.println(val);

        ArrayList<Object> list = new ArrayList<>();

        //Proposition
        //Class clazz= Proposition.class;
        //Constructor declaredConstructor = clazz.cons();


    }
}

//functional interface & annonymous implementation
@FunctionalInterface
interface Study {
    abstract int learn(String subject, int duration);
}
class BiologyMaterial implements Study {
    @Override
    public int learn(String subject, int duration)
    {
        if(subject == null)
            return duration;
        else
            return duration+1;
    }
}
class Scientist {
    public static void main(String[] courses) {

        final Study s = new BiologyMaterial() {};//method 1
        final Study s1 = new Study() {//method 2
            @Override
            public int learn(String sub, int dur) {
                return 0;
            }
        };
        Study s2 = (a,b)-> a==null? b : b+1;//method 3
        final Study s3 = new BiologyMaterial() {//method 4
            @Override
            public int learn(String s, int d) {
                return 1;
            }
        };
        System.out.print(s.learn(courses[0],
                Integer.parseInt(courses[1])));
    }
}

//inner static class instantiation & ArrayDeque & ScheduleExecutorService
class Ready {
    protected static int first = 2;
    private final short DEFAULT_VALUE = 10;
    static Ready re=new Ready();
    private static class GetSet {// extends Ready will stackoverflow
        int firsta = 5;
        //non-static field cannot be referenced from static context
        //int second2 = DEFAULT_VALUE;
        static int second = re.DEFAULT_VALUE;

        void gof(){
            System.out.println(first);
        }
    }
    private GetSet go = new GetSet();

    public static Mono<Object> flux1() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("flux1:");
        return Mono.just(1);
    }
    public static Mono<Object> flux2() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("flux2:");
        return Mono.just(2);
    }
    public static Mono<Object> flux3() throws InterruptedException {
        Thread.sleep(1000);

        int f=1/0;
        System.out.println("flux3:");
        return Mono.just(3);
    }

    private static ParallelFlux<Object> doSomething() throws InterruptedException {
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println("processor :"+i);
        return Flux.merge(flux1(), flux2(),flux3()).parallel(256)
                .map(string -> {
                    //System.out.println("dosomething string:"+string);
                    return string;
                });
    }
    public static void main(String[] begin) throws InterruptedException {

        System.out.println(java.time.LocalDateTime.now());
        List<Object> list = new ArrayList<>();
        doSomething().subscribe(mono-> {
            System.out.println("doOnComplete:"+mono);
            list.add(mono);
        });
        System.out.println(java.time.LocalDateTime.now());
        System.out.printf("final list: %s", list);
        System.out.println("=================================================");
        System.out.println("=================================================");

        Ready r = new Ready();
        //System.out.print(r.go.first);
        System.out.print(", "+r.go.second);

        ArrayDeque<Integer> d = new ArrayDeque<>();
        //d.offer(null);wi//ll cause java.lang.NullPointerException java.util.ArrayDeque.addLast
//        public void addLast(E e) {//source code
//            if (e == null)
//                throw new NullPointerException();
//            elements[tail] = e;
//            if ( (tail = (tail + 1) & (elements.length - 1)) == head)
//                doubleCapacity();
//        }
        d.offer(18);//[18]//offer add to front
        d.offer(5);//[5,18]
        d.push(13);//[5,18,13]//push to end
        //poll from end
        //d.element()//throw exeception
        System.out.println("ArrayDeque -> "+d.poll() + " " + d.poll());
        //poll from end
        //ArrayDeque -> 13 18

        String ak="haha";
        System.out.println("scheduleTask str set "+ak);
        scheduleTask(ak);
        ak="noprob";
        System.out.println("scheduleTask str reset "+ak);

    }
    static int schCount =0;
    static java.util.concurrent.ScheduledExecutorService executor;
    static void scheduleTask(String str){
        Runnable helloRunnable = new Runnable() {
            public void run() {
                System.out.println("scheduleTask str"+str);
                ++schCount;
                if(schCount>5){
                    executor.shutdown();
                    System.out.println("scheduleTask executor.shutdown()"+str);
                }
            }
        };
        executor = java.util.concurrent.
                Executors.newScheduledThreadPool(0);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3,
                java.util.concurrent.TimeUnit.SECONDS);
    }
}

 class Week {
    private static final String monday="";
    String tuesday;
    final static int wednesday = 3;
    final protected int thursday = 4;
}

//TreeSet & Comparable & Comparator
class Magazine implements Comparable<Magazine>
{//
    private String name;
    public Magazine(String name)
    {
        this.name = name;
    }

    public String toString() { return name; }
    public static void main(String[] args) {

        String ssss=new String("2");

        //ssss.charAt(2);//StringIndexOutOfBoundsException
        Comparator<Character> comp = (c1, c2)-> 0;//return 0 , all obj treated equal

        //comparator return int value
        //-1 : o1 < o2
        //0 : o1 == o2
        //+1 : o1 > o2

        TreeMap<Magazine,SnoopyClass> tm = new TreeMap();
        tm.put(new Magazine("a"),new SnoopyClass());
        tm.put(new Magazine("A"),new SnoopyClass());
        tm.keySet().stream().sorted();
        System.out.println("tm: "+tm);

        //comparator version
        Set<Magazine> s = new TreeSet<>(new Comparator<Magazine>() {
            @Override
            public int compare(Magazine o1, Magazine o2) {
                //if(o2==null || null==o1)return 0;
                int r=o1.name.compareTo(o2.name);
                System.out.println("compare[o1.name:"+o1.name+"] vs [o2.name:"+o2.name+"] r:"+r);
                return r;
            }
        });
        //s.add(null); //->//java.lang.NullPointerException at runtime
        s.add(new Magazine("highlights"));
        s.add(new Magazine("Newsweek"));
        s.add(new Magazine("highlights"));
        System.out.println(s.size());
        System.out.println(s.iterator().next());


        //comparable version
        Set<Magazine> set = new TreeSet<>();
        //java.lang.ClassCastException if class did not implements comparable
        // cannot be cast to java.lang.Comparable
        //set.add(null); //->//java.lang.NullPointerException at runtime
        set.add(new Magazine("highlights"));
        set.add(new Magazine("Newsweek"));
        set.add(new Magazine("highlights"));
        System.out.println(set.size());
        System.out.println(set.iterator().next());
    }
    public int compareTo(Magazine m) {
        return name.compareTo(m.name);
    }
//    @Override
//    public int compareTo(Magazine o) {
//        return 0;
//    }
}

//generic annonymous class inheritance
interface Comic<C> {
     int jo=0;
     void draw(C c);
}
class ComicClass<C> implements Comic<C> {
    public void draw(C c) {
        System.out.println(c);
    }
}
class SnoopyClass implements Comic<Snoopy> {
    public void draw(Snoopy c) {
        System.out.println(c);
    }
}
//class SnoopyComic implements Comic<Snoopy> {
//    public void draw(C c) {
//        System.out.println(c);
//    }
//}
class Wash<T> {
    T item;
    public void clean(T item)
    {
        System.out.println("Clean " + item);
    }
}
class Snoopy {
//    public String playMusic() {
//        System.out.print("Play!");
//        return "";
//    }
//    private static int playMusic() {
//        System.out.print("Music!");
//        return 0;
//    }
    public static void main(String[] args) throws InterruptedException {
        MyThread thread1 = new MyThread();
        Thread th1 = new Thread(thread1,"t1");

        Thread thread2 = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" thread2 b4 start");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" thread2 start");
        },"t2");
        th1.start();thread2.start();
        //thread2.join();


        System.out.println(Thread.currentThread().getName()+" main thread ->");
        int a=1/0;
        ArrayList ar=new ArrayList();
        Wash wash = new Wash();
        Wash<String> wash1 = new Wash<String>();
        Wash<String> wash2 = new Wash<>();
        Wash wash3 = new Wash<String>();

        Comic<Snoopy> c1 = c -> System.out.println(c);
        Comic<Snoopy> c2 = new ComicClass<>();
        Comic<Snoopy> c3 = new SnoopyClass();
        //Comic<Snoopy> c4 = new SnoopyComic();// class not compile
    }
}

class MyThread implements Runnable{

    volatile int ia;
    @Override
    public void run() {
        for(int i=0;++i<=1000;){
            ia+=1;
        }
        System.out.println(Thread.currentThread().getName()+" MyThread run"+ia);
    }
    public void start() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" MyThread start");
    }
}
class MyThread1 implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" MyThread1 run");
    }
    public void start() {
        System.out.println(Thread.currentThread().getName()+ " MyThread1 start");
    }
}

//generic naming
class News<_i_$1234567890_$$$$$> {}//same
class News1<T> {}class News2<t> {}//same

//****** generic wildcards rules ********
/*
-Adding an item to the list:
    List< ? extends X > doesn't allow to add anything, except for null into the list
    List< ? super X > allows to add anything that is-a X (X or its subtype), or null.
-Getting an item from the list:
    When you get an item from List< ? extends X >, you can assign it to a variable of type X or any supertype of X, including Object
    When you get an item from List< ? super X >, you can only assign it to a variable of type Object

eg.1
    List<? extends Number> list1 = new ArrayList<Integer>();
    list1.add(null);  //OK
    Number n = list1.get(0);  //OK
    Serializable s = list1.get(0);  //OK
    Object o = list1.get(0);  //OK

    list1.add(2.3);  //ERROR
    list1.add(5);  //ERROR
    list1.add(new Object());  //ERROR
    Integer i = list1.get(0);  //ERROR
eg.2
    List<? super Number> list2 = new ArrayList<Number>();
    list2.add(null);  //OK
    list2.add(2.3);  //OK
    list2.add(5);  //OK
    Object o = list2.get(0);  //OK

    list2.add(new Integer(1)); //OK
    list2.add(new Object());  //ERROR
    Number n = list2.get(0);  //ERROR
    Serializable s = list2.get(0);  //ERROR
    Integer i = list2.get(0);  //ERROR
* */
class ExtendingGenerics {
    private static <T extends Collection<U> , U> U add(T list, U element)
    {
        list.add(element);
        return element;
    }
    //super is a lower bound, and extends is an upper bound.
    //? super Number for reading, can read as Object type only
    //? super Number for writing, can write any subtype of Number
    static void minus(List<? super Number> list){
        list.add(new Integer(1));
        list.add(2);
        list.add(new Float(1.2));
        for (Object o : list) {

        }
    }
    //list
    //? extends Number for reading, can read as any supertype of Number(including Number)
    //? extends Number for writing, can not write things to Number bcz it can be list of any subtype
    static void minus1(List<? extends Number> list){
        for (Number number : list) {
            System.out.println(number);
        }
    }
    public static void main(String[] args) {
        List<String> values = new ArrayList<>();
        add(values, "duck");
        add(values, "duck");
        //add(values, String::new);//cannot compile
        add(values, new String());
        add(values, "goose");
        System.out.println(values);

        StringBuilder sb = new StringBuilder();//.insert(sb.length(),"");sb.length();
        sb.append("red");  sb.append(1);
        sb.deleteCharAt(0);
        System.out.println("deleteCharAt:"+sb);
        sb.delete(1, 9);
        System.out.println(sb);

        String sbs = "javas";


        //1. extends
        List<? extends Number> list1 = new ArrayList<Integer>();
        list1.add(null);  //OK
        //Integer nnum = list1.get(0);  //Error
        Number n = list1.get(0);  //OK
        Serializable s = list1.get(0);  //OK
        Object o = list1.get(0);  //OK

        list1.add(null);//OK
//        list1.add(2.3);  //ERROR
//        list1.add(5);  //ERROR
//        list1.add(new Object());  //ERROR
//        Integer i = list1.get(0);  //ERROR


        //2. super
        List<? super Number> list2 = new ArrayList<Number>();
        list2.add(null);  //OK
        list2.add(2.3);  //OK
        list2.add(5);  //OK
        Object o1 = list2.get(0);  //OK

        list2.add(new Integer(1)); //OK
//        list2.add(new Object());  //ERROR // add item that must be Number or subtype of Number
//        Number n1 = list2.get(0);  //ERROR
//        Serializable s1 = list2.get(0);  //ERROR
//        Integer i = list2.get(0);  //ERROR
        Object oi = list2.get(0);  //OK
    }
}

class Washx<T extends Collection> {
    T item;
    public void clean(T items) {
        System.out.println("Cleaned " + items.size() + " items");
    }
}
class LaundryTime {
    public static void main(String[] args) {
        Washx wash0 = new Washx();
        wash0.clean(Arrays.asList("sock", "tie"));

        Washx<List> wash = new Washx<List>();
        wash.clean(Arrays.asList("sock", "tie"));

        Washx<List> wash1 = new Washx();
        wash1.clean(Arrays.asList("sock", "tie"));

        Washx wash2 = new Washx<List>();
        wash2.clean(Arrays.asList("sock", "tie"));

        IntStream ints = IntStream.empty();
        IntStream moreInts = IntStream.of(66, 77, 88);
        Stream.of(ints, moreInts).flatMapToInt(x -> x).forEach(System.out::print);
    }
}

class TestOverride {
    static void doCalc(byte... a) {
        System.out.print("byte...");
    }
    static void doCalc(long a, long b) {
        System.out.print("long, long");
    }
    static void doCalc(Byte s1, int s2) {
        System.out.print("Byte, int");
    }
    static void doCalc(short s1, int s2) {
        System.out.print("short, int");
    }

    public static void main (String[] args) {

        //w7qqzdhap3uzz2303fq042x6g4u6ixbl&format=json
        getData("w7qqzdhap3uzz2303fq042x6g4u6ixbl","json");

        long bignu=2147483647l;
        byte b = 5;
        int gb = b << 1;//10
        System.out.println("gb(5) left shifted 1 :"+gb);
        doCalc(b, b);

        final Object exception = new RuntimeException();
        final Exception data = (Exception)exception;
        System.out.print(data);//java.lang.RuntimeException


    }


    public static void getData(String accesscode, String responseType) {
        String url = String.format("https://api.morningstar.com/v2/service/mf/ihc0q5ju746sozz8/universeid/axdsds59kqg203nv?accesscode=w7qqzdhap3uzz2303fq042x6g4u6ixbl&format=json");
        System.out.println(url);
        HttpGet get = new HttpGet(url);

        try (CloseableHttpClient client = createHttpClientForAPI();
             CloseableHttpResponse response = client.execute(get)) {
            int status = response.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(response.getEntity());
            if (status == 200) {
                System.out.println(content);
            } else {
                System.err.println(String.format("Call API failed, url:%s, status:%s, content:%s", url, status, content));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CloseableHttpClient createHttpClientForAPI() {
        // try once is enough
        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(1, false);
        // you may adjust these parameters based on your local environment after testing
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(20000)
                .build();
        // usually you do not need to set request configuration, let it as default unless you encounter access problem
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setRetryHandler(retryHandler)
                .build();
        return client;
    }
}

//Functional interface BiFunction
class Asteroid {
    public void mine(BiFunction<Double,Integer,Double> lambda) { // TODO: Apply functional interface

    }
    public static void main(String[] debris) {


        new Asteroid().mine((s,p) -> s+p);
        Stream<Integer> numStream = Stream.of(10, 20, 30);
        numStream.map(n -> n + 10).peek(s -> System.out.print(s)).forEach(s -> System.out.println(s));
        numStream.forEach(s -> System.out.println(s));
    }
}
//IntUnaryOperator
 class TicketTaker {
    private static void checkPrices(List<Double> prices,
                                    Consumer<Double> scanner) {
        prices.forEach(scanner);
    }
    private static int AT_CAPACITY = 100;
    //the following takes primitives, not generics
    //DoubleUnaryOperator -> double applyAsDouble(double operand);
    //LongUnaryOperator ->   long applyAsLong(long operand);
    //IntUnaryOperator//int applyAsInt(int operand);
    //UnaryOperator// takes in T and returns T   it extends Function -> R apply(T t);

    //DoubleConsumer -> primitives -> void accept(double value);
    //Consumer<Double>  -> generics can use in Stream.foreach()


    //ToDoubleBiFunction ->  double applyAsDouble(T t, U u);
    //DoubleSupplier ->  double getAsDouble();  : DoubleSupplier use double primitives cannot return null
    //Supplier<Double> -> T get(); : generics can return null
    //LongFunction ->  R apply(long value);
    //Function//Function<T, R>  //R apply(T t);
    //DoubleToIntFunction//  int applyAsInt(double value);
    //IntUnaryOperator  -> int applyAsInt(int operand);
    //ToIntBiFunction // int applyAsInt(T t, U u);
    //ToDoubleFunction// double applyAsDouble(T value);
    //ObjDoubleConsumer// void accept(T t, double value); //takes in T & double primitive
    //BiFunction<Double,Double,Double> takes in 2 Double -> return 1 Double apply
    //BinaryOperator<Double> takes in 2 Double -> return 1 Double //it extends BiFunction.
    //BiFunction<T, T, T> which accepts and returns the same type, can be replaced with BinaryOperator<T>.
    //DoubleFunction<Double>// R apply(double value); takes in 1 double primitives -> return 1 R (Double)
    public int takeTicket(int currentCount, java.util.function.IntUnaryOperator counter)//<Integer>
    {
        return counter.applyAsInt(currentCount);
    }
    public static void main(String...theater) {


        List<Double> prices = Arrays.asList(1.2, 6.5, 3.0);
        checkPrices(prices,
                p -> {
                    String result = p<5 ? "Correct" : "Too high";
                    result.replace(new StringBuffer("asd"),new StringBuffer("v"));
                    //result.substring(1,2) exclusive end index
                    System.out.println(result);
                });

        final TicketTaker bob = new TicketTaker();
        final int oldCount = 50;
        final int newCount = bob.takeTicket(oldCount,t -> {
            if(t>AT_CAPACITY) {
                throw new RuntimeException("Sorry, max has been reached");
            }
            return t+1;
        });
        List<Integer> lista=Arrays.asList(1, 2, 3);
        lista.stream().forEach(System.out::println);
        ArrayList<Integer> objects = new ArrayList<>(8);
        //  int newCapacity = oldCapacity + (oldCapacity >> 1);
        //               12 = 8           + (8/2)

        System.out.print(newCount);

        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<Integer>(new Integer[] { 1, 3, 5, 8 });
        //List<Integer> list = new ArrayList<>();list.add(1);list.add(2);list.add(3);
        //int n=1/0;
        Iterator itr = list.iterator();
        while (itr.hasNext()) {
            Integer no = (Integer)itr.next();
            System.out.println(no);
            if (no == 3)
                // This will not print,
                // hence it has created separate copy
                list.add(14);
        }
        System.out.println(list);
    }
}

//BiFunction param
class Dance {

    public class ConstantToken{
        public static final String token = "123";
    }
public static Integer rest(BiFunction<Integer,Double,Integer> takeABreak) {
    return takeABreak.apply(3, 10.2);
}

public static Consumer<HashMap<String,String>> consumerChain(){
    return map -> {

        map.put("1","1v");
        map.put("2","2v");

    };
}

public static void main(String[] participants) {

    HashMap<String,String> map = new HashMap<>();
    map.put("4", "4v"); map.put("5", "5v");map.put("ALL", "allv");
    Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
    while(iterator.hasNext()){
        Map.Entry<String, String> next = iterator.next();
        System.out.println("1=="+next.getKey());

    }
    iterator = map.entrySet().iterator();
    while(iterator.hasNext()){
        Map.Entry<String, String> next = iterator.next();
        System.out.println("2=="+next.getKey());

    }
    Consumer<Map<String,String>> printConsumer = list -> list.forEach((k,v)->{
        System.out.println(k+":k");
        System.out.println(v+":v");
    });
    consumerChain().andThen(m->{m.put("3","3v");}).andThen(printConsumer).accept(map);
    //rest((Integer n, Double e) -> (int)(n+e));
    //rest((n,w) -> (int)(n+w));
    //rest((s,w) -> (int)(2*w));
    rest((s,e) -> s.intValue()+e.intValue());

}
}

//DoubleToIntFunction
@JsonInclude(JsonInclude.Include.ALWAYS)
 class Bank {

    @JsonProperty("name")
    private String name;

    @JsonProperty("savingsInCents")
    private int savingsInCents;
    private static class ConvertToCents {

        static int gg=10;
        static DoubleToIntFunction f = p -> (int)(p*100);
        static DoubleFunction ff=p->{gg+=1;  return p*gg;};
    }
    public static long secondsAgo(java.util.Date datetime) {
        System.out.println("secondsAgo:"+datetime);
        Calendar date = Calendar.getInstance();
        date.setTime(datetime); // Parse into Date object
        Calendar now = Calendar.getInstance(); // Get time now
        long differenceInMillis = now.getTimeInMillis() - date.getTimeInMillis();
        long differenceInSeconds = (differenceInMillis) / 1000L; // Divide by millis/sec, secs/min, mins/hr
        return differenceInSeconds;
    }
    public static void main(String... currency) throws ParseException {

        Date thedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse("2022-08-21 22:00:00");
        //java.util.Date dt = new Date("2022-08-21T22:00:00");
        System.out.println(secondsAgo(thedate));

        Map<String,Object> map = new HashMap<String,Object>(){{put("student","1");}};

        Bank b = new Bank();
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map1 = mapper.convertValue(b, Map.class);
        map.putAll(map1);
        System.out.println(map);System.out.println(map1);


        System.out.println(Dance.ConstantToken.token);
        Bank creditUnion = new Bank();
        creditUnion.savingsInCents = 100;
        double deposit = 1.5;
        creditUnion.savingsInCents += ConvertToCents.f.applyAsInt(deposit); // j1
        creditUnion.savingsInCents += (double)ConvertToCents.ff.apply(deposit); // j2
        System.out.print(creditUnion.savingsInCents);
    }
}

//effectively final in lambda expression & stream api
 class DogSearch {
    //private static IntFunction<Integer> ;

    public static void printnn(Stream<String> stream) {
        Consumer<String> print = System.out::println;
        stream.peek(f-> System.out.println("f peek 1 "+f))
                .sorted((a,b)->a.length()-b.length())
                .peek(f-> System.out.println("f peek 2 "+f))
                .map(s -> {System.out.println("f map "+s);return s;})
                
                .peek(f-> System.out.println("f peek 3 "+f))

                .forEach(f-> System.out.println("f foreach "+f));
    }

    void reduceList(List<String> names, Predicate<String> tester) {
        names.removeIf(tester);
    }
    public static void main(String[] treats) {
        printnn(Stream.of("hi"));

        int MAX_LENGTH = 2;
        DogSearch search = new DogSearch();
        List<String> names = new ArrayList<>();
        names.add("Lassie"); names.add("Benji"); names.add("Brian");
        Object objarr=(Object)names;

        names = (ArrayList<String>)objarr;
        System.out.println(names.size()+" array");

        MAX_LENGTH += names.size();
        //search.reduceList(names, d -> d.length()>MAX_LENGTH);
        search.reduceList(names, d -> d.length()>5);
        //variable used in lambda expression should be final or effectively final
        System.out.println(names.size());

        List<Integer> list=Arrays.asList(1);
        Optional<Integer> any = list.stream()
                .filter(f->f==1).findFirst();//Optional<T>
                //.findAny();//returns Optional<T>
        Integer integer = any.get();
        final boolean b1 = list.stream()
                .anyMatch(a -> a == 0);

        OptionalDouble average = list.stream()
                .mapToInt(Integer::intValue).average();
                //.mapToObj(o->Integer.getInteger(""+o));

        //Optional null
        //1.empty optional returns false for isPresent()
        Optional<String> opt = Optional.empty();//new Optional<T>();
        opt.isPresent();//empty optional returns false



        //opt.orElseThrow()//Supplier<? extends X> exceptionSupplier
        //2. Optional.of(null);
        //Optional<String> optnull = Optional.of(null);//NullPointerException RuntimeException
        // private Optional(T value) {
        //        this.value = Objects.requireNonNull(value);
        //    }

        //3. Optional ofNullable
        Optional<String> optnullable = Optional.ofNullable(null);

        //if u pass null to ofNullable then underneath it create an empty Optional[new Optional<T>();]
        //empty optional isPresent is false
        // return value == null ? empty() : of(value);
        //optnullable.filter(/*Predicate<? super T> predicate*/) -> Optional<U>
        //optnullable.map(/*Function<? super T, ? extends U> mapper*/) -> Optional<U>
        //optnullable.flatMap(/*Function<? super T, Optional<U>> mapper*/) -> Optional<U>

        //Function<? super T, ? extends R> mapper

        System.out.println("BinaryOperator map======>");
        Stream<String> s = //Arrays.asList("speak", "bark", "meow", "growl").stream();
        Stream.of("speak", "bark", "meow", "growl");
        BinaryOperator<String> merge = (a, b) -> {
            System.out.println("merging a["+a+"] | b["+b+"]"); return a;};
        Map<Integer, String> map =
                s.collect(
                        Collectors.toMap(
                                k ->{  System.out.println("toMap k->"+k); return k.length();},
                                v ->{  System.out.println("toMap v->"+v); return v.toString();},
                                merge
                            )
                        );
        System.out.println(map);
        System.out.println(map.size() + " " + map.get(4));


        System.out.println("\n\n  === MERGE MAP ===  \n\n");
        //merging map
        //Merge HashMaps Example
//map 1
        HashMap<Integer, String> map1 = new HashMap<>();

        map1.put(1, "A");
        map1.put(2, "B");
        map1.put(3, "C");
        map1.put(5, "E");

//map 2
        HashMap<Integer, String> map2 = new HashMap<>();

        map2.put(1, "G");   //It will replace the value 'A'
        map2.put(2, "B");
        map2.put(3, "C");
        map2.put(4, "D");   //A new pair to be added

//Merge maps
        map2.forEach(
                (key, value) ->{
                    System.out.println("map1 merging key["+key+"] | value["+value+"]" );
                    map1.merge( key, value, (v1, v2) -> v1.equalsIgnoreCase(v2) ? v1 : v1 + "," + v2);
                }

        );

        System.out.println("map1====>"+map1);


        boolean present = average.isPresent();
        if(present){
            double asDouble = average.getAsDouble();
            //NoSuchElementException extends RuntimeException ,might throw
            System.out.println("find avg:"+asDouble);

        }else System.out.println("find avg empty:");



        Stream<Integer> stream = Stream.iterate(1, i -> i+1);
        //stream.forEach(System.out::println);
        boolean b = stream.anyMatch(i -> i > 5);
                //.anyMatch(i -> i > 5);
        System.out.println("stream anyMatch:"+b);

        //IntStream
        IntStream pages = IntStream.of(200, 300);
        IntSummaryStatistics stats = pages.summaryStatistics();
        long total = stats.getSum();
        long count = stats.getCount();
        System.out.println("IntSummaryStatistics : "+total + "-" + count);

        //Stream chaining
        Consumer<String> print = System.out::println;
        Stream<String> streamstr=Stream.of("1","2");
        streamstr.peek(print)
                .peek(print)//Intermediate peek can chain multiple together
                .map(sms -> sms+"a")
                .map(sms -> sms+"b")//Intermediate map can chain multiple together
                .peek(print)
                .forEach(print);//Terminal forEach cannot chain multiple

        System.out.println("\n grabbing char stream:");
        Stream<Character> streamchar = Stream.of('c', 'b', 'a'); // z1
        //streamchar.parallel().sorted().findAny().ifPresent(System.out::println);
        streamchar.sorted().findAny().ifPresent(System.out::println);//findAny() might return any bcz of its performance


        //partition by
        Stream<Boolean> bools = Stream.iterate(true, bv -> !bv);
        //List<Boolean> collect1 = bools.limit(1).collect(Collectors.toList());
        //System.out.println("collect1----=> "+collect1);//[true]
        Map<Boolean, List<Boolean>> mapa =
                bools.limit(1).collect(Collectors.partitioningBy(ba -> ba));
        System.out.println(mapa);//{false=[], true=[true]}


        //merge multiple collections via stream
        System.out.println("\n ====== merge multiple collections via stream start =====");
        Set<String> sets = new HashSet<>(); sets.add("tire-");
        List<String> lists = new LinkedList<>();

        Deque<String> queues = new ArrayDeque<>();
        queues.push("wheel-");
        Stream.of(sets, lists, queues)
                .flatMap(x -> x.stream())//flatMap must convert each collection to stream
                .forEach(System.out::print);// tire-wheel-
        System.out.println("====== merge multiple collections via stream end =====\n ");

        IntStream streams = IntStream.of(6, 10);


        OptionalInt min = streams.min();
        int asInt = min.getAsInt();
        System.out.println("asInt--> "+asInt);
        java.util.stream.LongStream longs = stream.mapToLong(i -> i);
        double asDouble = longs.average().getAsDouble();

        //sort stream
        System.out.println("\n ====== sort stream start Comparator.reverseOrder() ===== ");

        //Comparator.reverseOrder()
        Stream<String> ssort = Stream.of("aover the river", "cthrough the woods",
                "bto grandmother's house we go");
        Comparator<String> comparatorr=(s1,s2)->s1.compareTo(s2);
        Comparator<String> comparatorreverse=Comparator.reverseOrder();
        //<T extends Comparable<? super T>>
        //ssort.min();//Comparator is compulsory

        ssort.filter(n -> n.startsWith(""))
                //.sorted(Comparator.reverseOrder())//sorted Comparator cannot be used as method reference, bcz it needs 2 param
                .sorted((s1,s2)->s1.compareTo(s2))
                .sorted(comparatorreverse)
                //.sorted(comparatorr)
                .findFirst()
                .ifPresent(System.out::println);


        //partitioningBy() vs groupingBy()
        System.out.println("\n partitioningBy() vs groupingBy() start =====");
        /*partitioningBy will always return a map with two entries, one for where the predicate
        is true and one for where it is false. It is possible that both entries will have empty
        lists, but they will exist.

        That's something that groupingBy will not do, since it only creates entries when they are
        needed.

        At the extreme case, if you send an empty stream to partitioningBy you will still get two
        entries in the map whereas groupingBy will return an empty map.
        * */
        //1.partitioningBy -> more efficient
        Stream.of("I", "Love", "Stack Overflow")
                //Predicate<? super T> predicate
                .collect(Collectors.partitioningBy(sd -> sd.length() > 3))
                .forEach((k, v) -> System.out.println(k + " => " + v));
        //2.groupingBy
        Stream.of("I", "Love", "Stack Overflow")
                //Function<? super T, ? extends K> classifier
                .collect(Collectors.groupingBy(sa -> sa.length() > 3))
                .forEach((k, v) -> System.out.println(k + " => " + v));
        /*
        //groupingBy with other key
        Map<String, List<String>> studentsByCity = students.stream()
              .collect(Collectors.groupingBy(
                  Student::getCity,
                  Collectors.mapping(Student::getName, Collectors.toList())));
        * */
        System.out.println(" partitioningBy() vs groupingBy() end ===== \n");



        //flatMap IntStream
        System.out.println("\n flatMap IntStream  start =====");
        IntStream ints = IntStream.empty();
        IntStream moreInts = IntStream.of(66, 77, 88);
        Stream.of(ints, moreInts).flatMapToInt(x -> x).forEach(System.out::print);

        //LongStream Double getAverage
        System.out.println("\n Optional Double getAverage start =====");
        java.util.stream.LongStream streamLong = java.util.stream.LongStream.of(6, 10);
        LongSummaryStatistics statsLon = streamLong.summaryStatistics();
        //double asDouble = streamLong.average().getAsDouble(); //way 1
        double average1 = statsLon.getAverage();//way 2 , getter Sum, Max, Min, Average, Count from
        System.out.println(average1);

        //Optional of
        System.out.println("\n Optional of start =====");
        Optional<String> cupcake = Optional.of("Cupcake");

        //cupcake.orElse("");// return value != null ? value : other;
        //cupcake.orElseGet(()->"");//return value != null ? value : other.get();
        //cupcake.get(""); cannot pass in string param


        //infinite Stream:
        System.out.println("\n infinite Stream: start =====");
        //1.Stream.generate with Supplier
        //2.Stream.iterate(seed,UnaryOperator)
//        Stream<Character> chars = Stream.generate(() -> 'a');
//        chars.filter(c -> {
//                    System.out.println("infinite stream filter c:"+c);
//                    return c < 'b';})
//                .sorted()
//                //sorted is a stateful intermediate operation,
//                // it needs iterate all the elements b4 it completes sort logic,
//                // since infinite loop nvr completes, so this operation also never ended
//                .findFirst()
//                .ifPresent(System.out::print);

        //deque flatMap
        System.out.println("\n deque flatMap: start =====");
        Deque<String> queued = new ArrayDeque<>();
        queued.push("cat"); queued.push("dog");
        Stream.of(queued).flatMap(x -> x.stream()).forEach(f->System.out.print(f));//catdog
        Stream.of(queued).filter(x -> !x.isEmpty()).map(x -> x).forEach(System.out::print);//[dog, cat]
        queued.stream().filter(x -> !x.isEmpty()).map(x -> x).forEach(System.out::print);//catdog



    }
}

//Exception & Assertions
 class Igloo {
    public static void main(String[] bricks) {

        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            loader.setDefaultAssertionStatus(true);
            Class<?> c = loader.loadClass("com.atguigu.springcloud.service.TestService");
            TestService myObj = (TestService) c.newInstance();
            int flakes = 1;
            //double assert = 7.0;
            assert 1==2;
            TestService myObj1 = new TestService();
            System.out.println("1 myObj1=> "+myObj1);
            System.out.println("1 myObj=> "+myObj);
            myObj1=myObj;
            System.out.println("2 myObj1=> "+myObj1);
            System.out.println("2 myObj=> "+myObj);
            myObj=null;
            Runtime.getRuntime().gc();
            System.out.println("myObj1=> "+myObj1);
            System.out.println("myObj=> "+myObj);


            System.out.println("test assert passed");
            //assert flakes++>5:"flakes <= 5";
           // Assert.isTrue(flakes++>5);
        } catch (ClassCastException | ArithmeticException ec) {
            ec.printStackTrace();

        } catch (Exception ec) {
            System.out.println("Assert failed exception:");
            ec.printStackTrace();
        }catch (Error err) {//need enable assertion in vm option  [-en]
            //to enable java.lang.AssertionError to be thrown
            System.out.println("Assert failed Error:");
            err.printStackTrace();
        }
    }
}

//Exception constructor
//***1.class must inherit from RunTimeException or Error to be Considered an unchecked exception
 class WhaleSharkException extends Exception {
    public WhaleSharkException() {
        super("Friendly shark!");
    }
    public WhaleSharkException(String message) {
        super(new Exception(new WhaleSharkException()));
    }
    public WhaleSharkException(Exception cause) {
        //super();// automatically injected if did not write this line
    }
    public WhaleSharkException(Exception cause,String msg) {
        super(new StackOverflowError()); //accept a throwable
        //IOException
    }
}

//try-with-resources
//***1.class must inherit from RunTimeException or Error to be Considered an unchecked exception, others like inherit from Throwable , etc. are checked exception
//The main point of try-with-resources is to make sure resources are closed reliably
// without possibly losing information.
//Closeable -> IOException
//AutoCloseable -> Exception
//2.try-with-resources , if try block has the exception(primary exception),
// then the close() method exception is suppressed(if it has exception in close() method)
 class PrintCompany {
    class Printer implements Closeable//Closeable extends AutoCloseable
    { // r1
        public int ak=0;
        public String instanceName;
        public Printer(String instanceNmae_){
            super();
            Printer.this.ak=1;// same as ak=1;
            if(ak>0){
                ak=100;
            }
            instanceName=instanceNmae_;
            System.out.println("Printer init ak :"+ak);
        }
         public void print() {
             System.out.println("try-with-resources");
         }
         public void close() throws IOException {
             //super();
             System.out.println("close exception thrown intentionally from :"+instanceName);
            throw new IOException("close exception...");
         }
         //close() method executed in the reverse order in which order the try-with-resources
        //are declared
    }
    public void printHeadlines() {
        Printer p0 = new Printer("p");
        Printer p10 = new Printer("p1");

        try (Printer p = new Printer("p");//can have multiple resources

             //must implements Closeable/AutoCloseable
             //the resource must implements Closeable interface
             Printer p1 = new Printer("p1")) { // r2

            //AutoCloseable
             p.print();
             //throw new RuntimeException();
        }//note the resource close method is run right after this try {} ends b4 the catch/finally below
        //try-with-resources can without the catch block & finally block
//        finally {
//
//        }
//        catch (WhaleSharkException | RuntimeException  ec){//order doesn't matter
//            //it must be possibility for the declared exception to throw, else cannot compile
//            //ec=new WhaleSharkException();-> ec is effectively final, cannot reassign
//            System.out.println(ec);
//        }
        catch (IOException  ec){
            //SQLException
            //IllegalStateException -> RunTimeException
            System.out.println("catching ioException from close():"+ec);
        }
    }
    public static void main(String[] headlines) {
        new PrintCompany().printHeadlines(); // r3

    }
}

//Chapter 18: Java I/O Fundamentals
//Writer is an abstract class, InputStream,OutputStream,Reader are all abstract class

class Smoke {
    public void sendAlert(File fn) {
        try(
                BufferedWriter w = new BufferedWriter(
                new FileWriter(new File("myfile.txt")))
        ) {
            w.write("ALERT!");
            w.flush(); w.write('!');
            System.out.print("1");

            Console console_ = System.console();
            int read = console_.reader().read();//-1 if EOF
            String s = console_.readLine();
            char[] chars = console_.readPassword();


        }
        catch (IOException e) {
            System.out.print("2");
        }
        finally {
            System.out.print("3");
        }
    }
    public static void main(String[] testSignal) throws IOException{
        //new Smoke().sendAlert(new File("alarm.txt"));

        //file creation
        File f1 = new File("./test_templates/proofs");//at proj root path
        //  use /test_template -> system root path
        //  use ./test_template -> project root path

        System.out.println(f1.mkdirs());//true -> must be mkdirs, mkdir cannot create multiple dir(return false)
        File f2 = new File("./test_templates");
        System.out.println(f2.mkdir());//false -> bcz already existed
        System.out.println(new File(f2, "draft.txt")
                .createNewFile());//true -> if file already existed -> false
        System.out.println(f1.delete());//true
        System.out.println(f2.delete());//false -> failed to delete bcz inside has 1 file draft.txt
        for (File file : f2.listFiles()) {//f2.list() -> file name only
            //file.delete()//security.checkDelete(path)

            System.out.println("iterating file:"+file.getAbsolutePath());//.getAbsolutePath()
//E:\proj\atguigu_spirngcloud2020-master\atguigu_spirngcloud2020-master\springCloud2020\.\test_templates\draft.txt
        }

        //file separator (system independent)
        System.out.println("java.io.File.separator:"+java.io.File.separator);  //-> fs.getSeparator(); // "\"(windows)
        System.out.println("java.io.File.separatorChar:"+java.io.File.separatorChar);//-> fs.getSeparator(); // "\"(windows)
        System.out.println("java.io.File.pathSeparator:"+java.io.File.pathSeparator); //-> ""+ fs.getPathSeparator() // ";"(windows)
        System.out.println("java.nio.file.FileSystems.getDefault().getSeparator():"+
                java.nio.file.FileSystems.getDefault().getSeparator()); //-> since java 7 // "\"(windows)
        //new File(new String()).separatorChar  //-> fs.getSeparator();
        System.out.println("System.getProperty file.separator: "+System.getProperty("file.separator"));// "\"(windows)
        System.out.println("System.getProperty path.separator: "+System.getProperty("path.separator"));// ";"(windows)

        Properties properties = System.getProperties();
        System.out.println("System.getProperties: "+properties);//all properties


        //file object file system creation
        File cake = new File("cake.txt");//this line will not create cake.txt
        Writer pie = new FileWriter("pie.txt");//this line already created pie.txt
        pie.write("test");
        pie.flush();
        pie.close();
        new File("fudge.txt").mkdirs();
        System.out.println("file object file system creation done");


    }
}

class Roster {
    protected void printRoster(Path p) throws IOException {
        for(Path f :
                Files.list(p).collect(Collectors.toList())
        ) { // n1
            if(f.toString().endsWith(".per")) // n2
                 System.out.print(f);
        }
    }
    public static void main(String... volunteers) throws IOException {
        new Roster().printRoster(Paths.get(volunteers[0]));
    }
}

class PrimeReader {
     /*InputStream mark():
     -   The general contract of mark is that,
         if the method markSupported returns true, the stream somehow remembers all
         the bytes read after the call to mark and stands ready to supply those same
         bytes again if and whenever the method reset is called. However,
         the stream is not required to remember any data at all
         if more than readlimit bytes are read from the stream before reset is called.
     -   Marking a closed stream should not have any effect on the stream
     * */
    public static void main(String[] real) throws Exception {
        //BufferedInputStream constructor takes in InputStream
        //FileReader extends InputStreamReader

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        //InputStream is = cl.getResourceAsStream("prime6.txt");
        //this.getClass().getClassLoader().getResourceAsStream("file.name");
        try (InputStream is = cl.getResourceAsStream("prime6.txt")) {
            is.skip(1); is.read();//read the next byte called native method read0()
            is.skip(1); is.read();
            //InputStream markSupported() method default return false
            // & FileInputStream did not override markSupported() so it also return false
            // use BufferedInputStream, it overriden markSupported() to return true
            is.mark(4);//markSupported can override to enable to true, default is false
            //if marked then later call reset can start read from the mark position
            //if read limit is reached then reset will not become effective
            is.skip(1);
            is.reset();
            //reset() throws IOException  if this stream has not been marked or if the
            //mark has been invalidated(read  exceed read limit after mark at the position).
            //or mark is not supported in the InputStream like FileInputStream
            System.out.print(is.read());

            //sun.misc.SharedSecrets.getJavaSecurityAccess().doIntersectionPrivilege()
            //Console console1 = sun.misc.SharedSecrets.getJavaIOAccess().console();//if jvm console is still null then this get called
            Console console = System.console();// sun.misc.SharedSecrets.getJavaIOAccess().console();/
        }
    }
}

//InputStream read
class Pidgin {
    public void copyPidgin(File s, File t) throws Exception {
        try(InputStream is = new FileInputStream(s);
            OutputStream os = new FileOutputStream(t)) {
            byte[] data = new byte[123];
            int chirps;
            while((chirps = is.read(data))>0) {
                //os.write(data);// this might skip some data
                //bcz write(data) it will always write the entire byte data array,
                //which may occur at last iteration of copying, results that files whose
                //size are multiple of 123 will be copied correctly, while other files will
                //be written with extra data appended to the end of file
                os.write(data,0,chirps);// use this to write all data read
            }
        }
    }
}



class Valve implements Serializable {
    private int chambers = -1;
    private transient Double size = null;
    private static String color="black";
    public Valve() {
        this.chambers = 3;
        color = "BLUE";
    }
    public static void main(String[] love) throws Throwable {
        try (ObjectOutputStream o = new ObjectOutputStream(
                new FileOutputStream("."+ File.separator+"scan.txt")))
        {
            final Valve v = new Valve();
            v.chambers = 2;
            v.size = 10.0;
            v.color = "RED";
            o.writeObject(v);
        }
        new Valve();
        try (ObjectInputStream o = new ObjectInputStream(
                new FileInputStream("."+ File.separator+"scan.txt")))
        {
            Valve v = (Valve)o.readObject();
            System.out.print(v.chambers+","+v.size+","+v.color);
        }
    }

    {
        chambers = 4;
    }
}

//Chapter 19: Java File I/O NIO.2
//System console reader & writer
class InconvenientImplementation {
    public static void tendGarden(Path p) throws Exception {
        /*
        if you need to filter out files/dirs by attributes - use Files.find(),
        if you don't need to filter by file attributes - use Files.walk().
        * */
        Files.walk(p,1)
                //.map(pa -> pa.toRealPath())//unhandled exception
                .map(pa -> {
                    return pa.toAbsolutePath();//this method does not throw if written in this manner
//                    try {//lambda expression if has checked exception need handle inside lambda
//                        return pa.toRealPath();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return ""+e.getMessage();
//                    }
                })
                .forEach(System.out::println);
    }
    public static void printNotes() {
        try (OutputStream out = System.out) { // y1


            Files.copy( Paths.get("pom.xml"),out);


            //1. Files.copy(Path path,Path path1,CopyOption option)
            //2. Files.copy(Path path,OutputStream out)
            //3. Files.copy(InputStream in,Path path,CopyOption option)

            System.out.println("Files.copy from path done ======================");
            //invoke Files copy private static method
            //Files.copy(fis,out); //is private
            File fe=new File("pom.xml");
            InputStream fis=new FileInputStream(fe);

            java.lang.reflect.Method m
                    = Files.class.getDeclaredMethod("copy",
                    InputStream.class,OutputStream.class);
            m.setAccessible(true); //if security settings allow this
            Object o = m.invoke(null, fis,out); //use null if the method is static


            System.out.println("Files.copy static reflection private done ["+o.hashCode()+"] ======================"+ o.toString());
            System.out.println("Files.copy static reflection private done result ======================"+ (Long)o);
            //5.75KB = 1024 B * 5.75 = 5888 B

            //Usage Example: Suppose we want to change a file's last access time.
            //          Path path = ...
            //          FileTime time = ...
            //          Files.getFileAttributeView(path, BasicFileAttributeView.class)
            //          .setTimes(null, time, null);
            BasicFileAttributeView dfav=new DosFileAttributeView() {
                @Override
                public String name() {
                    return null;
                }

                @Override
                public DosFileAttributes readAttributes() throws IOException {
                    return null;
                }

                @Override
                public void setReadOnly(boolean value) throws IOException {

                }

                @Override
                public void setHidden(boolean value) throws IOException {

                }

                @Override
                public void setSystem(boolean value) throws IOException {

                }

                @Override
                public void setArchive(boolean value) throws IOException {

                }

                @Override
                public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {

                }
            };

            //Files.copy(InputStream source, OutputStream sink)
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String... dontDoThis) throws Exception {
        Console c = System.console();


        //Path only have toRealPath() & register() throws IOException
        Path p_parent = Paths.get("../sang").getParent();
        tendGarden(Paths.get("./oktest123"));
        Path parentParent = p_parent.getParent();
        System.out.println("p_parent:"+p_parent);
        System.out.println("parentParent:"+parentParent);

        //Paths.get("../sung").getRoot().getParent();
        // relative path getRoot & getParent both returns null
        System.out.println("getroot ====> "+Paths.get("/sung").getRoot());//  =>  /
        System.out.println("getparent ====> "+Paths.get("/sung").getParent());// =>  /

        File snf=new File("");
        System.out.println(snf.isDirectory());//false
        System.out.println(snf.isAbsolute());//false
        System.out.println(snf.isFile());//false
        System.out.println(snf.isHidden());//false
        Path path_=snf.toPath();
        for (Path path : snf.toPath()) {
            System.out.println(path.toAbsolutePath());
            //E:\proj\atguigu_spirngcloud2020-master\atguigu_spirngcloud2020-master\springCloud2020
            //Path pathOther=path.resolve(Path other);//from this path to resolve the subpath
        }
        //Files.delete(new Path); -> void

        System.out.println("battributes => ");
        Path file = Paths.get("pom.xml");//.getParent();
        java.nio.file.attribute.BasicFileAttributes battributes =
                Files.readAttributes(file, BasicFileAttributes.class);// BasicFileAttributes.class
        // //java.nio.file.NoSuchFileException if file not exists
        System.out.println("battributes: "+battributes.lastModifiedTime());

        //1.Files.lines() : use BufferedReader -> lines() -> Iterator<String> -> StreamSupport.stream()
        //Files.lines(Path path) -> Stream<String> //throws IOException //faster
        //like all streams lines() loads the contents of the file in a lazy manner, time
        // it takes is constant regardless of the file size
        // ****the stream only start traverse when terminal operation triggers
        //
        //2.Files.readAllLines() : use BufferedReader try-with-resources reader.readLine();
        //Files.readAllLines(Path path) -> List<String> //throws IOException //slower

        //3. Files.list(Path dir) -> Stream<Path> : use StreamSupport.stream iterator



        //normalize()
        //The normalize() method of java.nio.file.Path used to return a path from current
        // path in which all redundant name elements are eliminated.
//        Path path
//                = Paths.get("D:\\..\\..\\.\\p2\\core"
//                + "\\cache\\binary");
//
//        // print actual path
//        System.out.println("Actual Path : "
//                + path);//D:\..\..\.\p2\core\cache\binary
//
//        // normalize the path
//        Path normalizedPath = path.normalize();
//
//        // print normalized path
//        System.out.println("\nNormalized Path : "
//                + normalizedPath);//D:\p2\core\cache\binary


        //java.nio.file.File not exist
        //java.nio.file.Path  : interface
        //java.nio.file.Paths.get() -> Path //Paths is a  final class

        //java.nio.file.Files.walk()//depth first traversal
        //java.nio.file.Files.find()//depth first traversal
        ///java.nio.file.Files.isDirectory(Path path, LinkOption... options) //Files is a final class
        //java.nio.file.Files.isRegularFile(Path path, LinkOption... options)
        //java.nio.file.Files.isHidden(Path path)
        //nio file
        java.nio.file.Path path_1=new Path() {
            @Override
            public FileSystem getFileSystem() {
                return null;
            }

            @Override
            public boolean isAbsolute() {
                return false;
            }

            @Override
            public Path getRoot() {
                return null;
            }

            @Override
            public Path getFileName() {
                return null;
            }

            @Override
            public Path getParent() {
                return null;
            }

            @Override
            public int getNameCount() {
                return 0;
            }

            @Override
            public Path getName(int index) {
                return null;
            }

            @Override
            public Path subpath(int beginIndex, int endIndex) {
                return null;
            }

            @Override
            public boolean startsWith(Path other) {
                return false;
            }

            @Override
            public boolean startsWith(String other) {
                return false;
            }

            @Override
            public boolean endsWith(Path other) {
                return false;
            }

            @Override
            public boolean endsWith(String other) {
                return false;
            }

            @Override
            public Path normalize() {
                return null;
            }

            @Override
            public Path resolve(Path other) {
                return null;
            }

            @Override
            public Path resolve(String other) {
                return null;
            }

            @Override
            public Path resolveSibling(Path other) {
                return null;
            }

            @Override
            public Path resolveSibling(String other) {
                return null;
            }

            @Override
            public Path relativize(Path other) {
                return null;
            }

            @Override
            public URI toUri() {
                return null;
            }

            @Override
            public Path toAbsolutePath() {
                return null;
            }

            @Override
            public Path toRealPath(LinkOption... options) throws IOException {
                return null;
            }

            @Override
            public File toFile() {
                return null;
            }

            @Override
            public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
                return null;
            }

            @Override
            public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) throws IOException {
                return null;
            }

            @Override
            public Iterator<Path> iterator() {
                return null;
            }

            @Override
            public int compareTo(Path other) {
                return 0;
            }
        };
        //path_.relativize()
        //Path path = FileSystems.getDefault().getPath("");
        //new File(fileName).toPath()

        //path_.subpath().getName(int index)
        // getName(int index) : The element that is closest to the root in the directory
        // hierarchy has index 0.
        // The element that is farthest from the root has index count-1.


        printNotes();
        //return;
        if(c != null) {
            c.writer().write('P');
            c.writer().write('a');
            c.writer().write('s');
            c.writer().write('s');
            c.writer().flush(); // t1
            int i;
            StringBuilder sb = new StringBuilder();
            while((i = c.reader().read()) != 'x') { // t2
                sb.append((char)i);
            }
            c.writer().format("Result: %s",sb.toString());
        }
    }
}
class Sneaker { //java.nio
    public void setupInventory(Path desiredPath) throws Exception {
        Path suggestedPath = Paths.get("sneakers");
        //EnumSet.of(FOLLOW_LINKS)
        if(Files.isSameFile(suggestedPath, desiredPath) // j1
                //isSameFile 1st chk if path string is same with equals
                //then moves on chk if 2 file object reference on same file system obj
                //throws NoSuchFileException if not exists
            && !Files.exists(suggestedPath))
            Files.createDirectories(desiredPath); // j2
    }
    public static void main(String[] socks) throws Exception {
        //java.io.File
        System.out.println(Files.isSameFile(FileSystems.getDefault().getPath("abc123"),Paths.get("abc123")));

        Path w = new File("/stock/sneakers").toPath(); // j3
        new Sneaker().setupInventory(w);
    }
}

 class Guitar {
    public void readMusic(File f) throws IOException {
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
             String music = null;
            try {
                while((music = r.readLine()) != null)
                    System.out.println(music);
            } catch (IOException e) {}
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {}
    }
}

//nio rewriter
class Rewriter {
    public static void copy(Path source, Path target) throws Exception {
        try (BufferedReader r = Files.newBufferedReader(source);
             Writer w = Files.newBufferedWriter(target))
        {
            String temp = null;
            while((temp = r.readLine()) != null ) {
                w.write(temp+"\n");

            }
        }
    }
    public static void main(String[] tooMany) throws Throwable {
        Rewriter.copy(Paths.get("/original.txt"),
                FileSystems.getDefault().getPath("/","unoriginal.txt"));
    }
}

//read path count
class Surgeon {
    public static void updateFileAttribute(Path folder, Path file) throws IOException {
        Path p = folder.resolve(file);
        BasicFileAttributeView vw = Files.getFileAttributeView(p,
                BasicFileAttributeView.class);

        //FileTime
        /*
        * */
        if(vw.readAttributes().creationTime().toMillis()<System.currentTimeMillis()) {
            vw.setTimes(//if any of the time is null, then setting that time has no effect
                    FileTime.fromMillis(System.currentTimeMillis()),//FileTime lastModifiedTime,
                    FileTime.fromMillis(System.currentTimeMillis()),//FileTime lastAccessTime,
                    FileTime.fromMillis(System.currentTimeMillis())//FileTime createTime
            );
        }
    }
    public Path rebuild(Path p) {
        Path v = null;
        int l=p.getNameCount();
        /*/tissue/heart/chambers.txt
        getName path[0]:tissue
        getName path[1]:heart
        getName path[2]:chambers.txt
        * */
        for(int i=0; i<l; i++)
            if(v==null){
                v = p.getName(i);
                System.out.println("getName path["+i+"]:"+v);
            }
            else {
                System.out.println("getName else path["+i+"]:"+p.getName(i));
                v = v.resolve(p.getName(i));
            }
            return v;
    }
    public static void main(String... tools) throws Exception{
        final Surgeon al = new Surgeon();
        Path original = Paths.get("/tissue/heart/chambers.txt");
        System.out.println(original.toAbsolutePath());//E:\tissue\heart\chambers.txt
        Path repaired = al.rebuild(original);
        System.out.println(repaired.toAbsolutePath());//E:\proj\atguigu_spirngcloud2020-master\atguigu_spirngcloud2020-master\springCloud2020\tissue\heart\chambers.txt
        System.out.print(original.equals(repaired));

        System.out.println("Path normalize ====================================");
        Path v1 = Paths.get("/./desert/./").resolve(Paths.get("sand.doc"));
        Path v2 = new File("/desert/./cactus/../sand.doc").toPath();
        Path v1_norm=v1.normalize();
        Path v2_norm=v2.normalize();
        System.out.println("v1 :"+v1);//\.\desert\.\sand.doc
        System.out.println("v2 :"+v2);//\desert\.\cactus\..\sand.doc
        System.out.println("v1 absolute path:"+v1.toAbsolutePath());//E:\.\desert\.\sand.doc
        System.out.println("v2 absolute path:"+v2.toAbsolutePath());//E:\desert\.\cactus\..\sand.doc
        System.out.println("v1_norm:"+v1_norm);//\desert\sand.doc
        System.out.println("v2_norm:"+v2_norm);//\desert\sand.doc
        //System.out.print(Files.isSameFile(v1,v2));//throws exception if file not exists
        System.out.print(" "+v1.equals(v2));
        System.out.print(" "+v1_norm.equals(v2_norm));
    }
}


//Chapter 20 , Oracle OCP Concurrency
class TicketTakerConcurrency {
    long ticketsSold;
    final AtomicInteger ticketsTaken;
    public TicketTakerConcurrency() {
        ticketsSold = 0;
        ticketsTaken = new AtomicInteger(0);
    }
    public void performJob() {

        CountDownLatch latch = new CountDownLatch(100);
        for(int i=-1;++i<10;){
            new Thread(()->{
                IntStream.iterate(1, p -> p+1)
                        .parallel()
                        .limit(10)
                        .forEach(ii -> {
                            latch.countDown();
                            ticketsTaken.getAndIncrement();
                            System.out.println("ticketsTaken:"+ticketsTaken);
                        });

                IntStream.iterate(1, q -> q+1)
                        .limit(5)
                        .parallel()
                        .forEach(ii -> {
                            //latch.countDown();
                            ++ticketsSold;
                            System.out.println("ticketsSold:"+ticketsSold);
                        });
            },String.valueOf(i)).start();
        }

        try {
            latch.await();
        } catch(InterruptedException ex) {
            System.out.println(ex);
        } catch(IllegalArgumentException | ClassCastException ex1) {
            System.out.println(ex1);
        }
        System.out.println(ticketsTaken+" "+ticketsSold);
    }
    public static void main(String[] matinee) throws ExecutionException, InterruptedException {
        new TicketTakerConcurrency().performJob();

        //Executor//Executor is interface -> void execute(Runnable command);

        ExecutorService cache= Executors.newCachedThreadPool();
        //Runnable functional interface -> public abstract void run();

//        Executors.newCachedThreadPool();
//        Executors.newFixedThreadPool(1);
//        Executors.newScheduledThreadPool(1);
//        Executors.privilegedThreadFactory();
//        Executors.newSingleThreadExecutor();
//        Executors.newSingleThreadScheduledExecutor();
//        Executors.newWorkStealingPool();
        //Executors.unconfigurableExecutorService()

        //scheduledExecutorService.
        ArrayList<Integer> original = new ArrayList<>(Arrays.asList(1,2,3,4,5));

        List<Integer> original_copy = new ArrayList<>(original);
        for(Integer q : original) original_copy.add(q);

        List<Integer> copy1 = new CopyOnWriteArrayList<>(original);
        for(Integer w : copy1) copy1.add(w);

        System.out.println("copy1 size:"+copy1.size());
        System.out.println("original size:"+original.size());
        System.out.println("original_copy => "+original_copy);


        System.out.println("synchronizedList b4 original=> "+original);
        List<Integer> copy2 = Collections.synchronizedList(original);
        copy2.removeIf(r->true);//note this remove item from original as well
        //for(Integer q : copy2) copy2.add(4);//concurrentModification
        System.out.println("synchronizedList copy2=> "+copy2);
        System.out.println("synchronizedList original=> "+original);

//        Iterator<Integer> iterator2 = copy2.iterator();
//        while(iterator2.hasNext()){
//            copy2.remove(iterator2.next());
//        }
        //for(Integer w : copy2) copy2.remove(w);// ConcurrentModification
        System.out.println("copy2 size:"+copy2.size());
        System.out.println("copy2 remove chk original:"+original);

        List<Integer> original1 = new ArrayList<>(Arrays.asList(1,2,3,4,5));
        List<Integer> copy3 = new ArrayList<>(original1);
        Iterator<Integer> iterator = copy3.listIterator();

        while(iterator.hasNext()){
            Integer next = iterator.next();
            System.out.println("removing iter copy3 next:"+next);
            iterator.remove();

            //break;
        }



        //for(Integer w : copy3) copy3.remove(w);// ConcurrentModification
        System.out.println("copy3 size:"+copy3.size());

        Queue<Integer> copy4 = new ConcurrentLinkedQueue<>(original);
        for(Integer w : copy4) copy4.remove(w);
        System.out.println("copy4 size:"+copy4.size());
        System.out.println("original size:"+original.size());

        //Deque can add to both head(push()) and end(offer()), poll from head
        Deque<Integer> copy5 = new ConcurrentLinkedDeque<>(Arrays.asList(1,2,3,4,5));
        for(Integer w : copy5) {//iterator is from haed loop to end, if here we change push() to offer, then it will cause infinite loop
            copy5.push(w);//push to haed
        }
        copy5.offer(999);//offer to end
        System.out.println("copy5.peek head:"+copy5.peek());
        System.out.println("copy5 :"+copy5);//[5, 4, 3, 2, 1, 1, 2, 3, 4, 5, 999]

        Queue<Integer> copy6 = new ConcurrentLinkedQueue<>(Arrays.asList(1,2,3,4,5));
//        for(Integer w : copy6) {
//            copy6.offer(w);
//        }

        //queue is FIFO, add to end only, poll from head
        copy6.offer(888);//both add to end
        copy6.add(999);//add to end
        System.out.println("copy6.peek head:"+copy6.peek());
        System.out.println("copy6 :"+copy6);//[1, 2, 3, 4, 5, 888, 999]

        //submitReports();
    }

    public static void submitReports() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        Callable<Integer> calla=new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Future callable");
                return 1;
            }
        };
        Future bosses = service.submit(() -> {
            System.out.println("Future runnable");
            //return 2;// note if without return this becomes runnable anonymous classs
        });
        Future bosses1 = service.submit(calla);
        service.shutdown();
        System.out.println(bosses1.get());
        System.out.println(bosses.get());

        ExecutorsShutdownCompare();
    }

    public static void ExecutorsShutdownCompare() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("interrupted");
                        break;
                    }
                }
            }
        });

        //executor.shutdown();
        executor.shutdownNow();//will interrupt current thread
        if (!executor.awaitTermination(100, TimeUnit.MICROSECONDS)) {
            System.out.println("Still waiting after 100ms: calling System.exit(0)...");
            System.exit(0);
        }
        System.out.println("Exiting normally...");
    }

}

//CyclicBarrier & parallelStream & ForkJoin
class CartoonCat {

    static class Fibonacci extends RecursiveTask<Integer> {
        final int n;
        public Fibonacci(int n_) {  this.n = n_; }//this.

        @Override
        protected Integer compute() {
          if (n <= 1)
            return n;
          Fibonacci f1 = new Fibonacci(n - 1);
          f1.fork();
          Fibonacci f2 = new Fibonacci(n - 2);
          //f2.fork();
            //invokeAll(f1,f2);
          return f2.compute() + f1.join();
        }
        public  void init(){//int num
            ForkJoinPool fjp = new ForkJoinPool();
            //Fibonacci fi=new Fibonacci(num);
            int fibonacci = fjp.invoke(this);
            System.out.printf("fibonacci[%d] : "+fibonacci,n);
        }
    }
    public void ForkJoinExample()
    {
        ForkJoinPool fjp = new ForkJoinPool();

        double[] nums = new double[5000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = (double)(((i % 2) == 0) ? i : -1);
        }
        Sum task = new Sum(nums, 0, nums.length);
        double summation = fjp.invoke(task);
        System.out.println("Summation " + summation);
    }
    class Sum extends RecursiveTask<Double> {
        final int seqThreshold = 500;
        double[] data;
        int start, end;

        Sum(double[] data, int start, int end)
        {
            this.data = data;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Double compute()
        {
            double sum = 0;
            if ((end - start) < seqThreshold) {
                for (int i = start; i < end; i++)
                    sum += data[i];
            }
            else {
                int middle = (start + end) / 2;

                Sum subtaskA = new Sum(data, start, middle);
                Sum subtaskB = new Sum(data, middle, end);

                subtaskA.fork();
                subtaskB.fork();

                sum += subtaskA.join() + subtaskB.join();
            }
            return sum;
        }
    }

    private void await(CyclicBarrier c) {
        try {
            System.out.println(Thread.currentThread().getName()+":getParties:"+c.getParties());
            System.out.println(Thread.currentThread().getName()+":getNumberWaiting:"+c.getNumberWaiting());

            c.await();
            System.out.println(Thread.currentThread().getName()+":c.await after== runs after CyclicBarrier declared runnable");

        } catch (Exception e) {}
    }
    public void march(CyclicBarrier c) {
        ExecutorService s =
                //Executors.newSingleThreadExecutor();//single thread will free the application
        Executors.newFixedThreadPool(4);//need at least 4 thread
        //Executors.newCachedThreadPool();
        //note if CyclicBarrier thread limit has nvr reached, then application enters in deadlock state
        for(int i=0; i<12; i++)
        {
            s.execute(() -> await(c));
        }
        s.shutdown();
    }
    public static void main(String... strings) {
        //1.CyclicBarrier eg
//        new CartoonCat().march(new CyclicBarrier(
//                4, //need at least 4 thead or else application will be freezed
//                () -> System.out.println(Thread.currentThread().getName()+":Ready")));

        //2.parallelStream eg
        //Arrays.asList(1,2,3,4).stream().forEach(System.out::println);//result is consistent 1,2,3,4
        //Arrays.asList(1,2,3,4).parallelStream().forEach(System.out::println);//result is not consistent
        //Arrays.asList(1,2,3,4).parallelStream().forEachOrdered(System.out::println);//result is consistent bcz parallelStream

        //3.ForJoin eg
        //ForkJoinTask implements Future<V> & Serializable has no compute() method
        //abstract class RecursiveTask<V> extends ForkJoinTask<V> has compute() method return value
        //abstract class RecursiveAction extends ForkJoinTask<Void> has compute() method return void
//        CartoonCat cc=new CartoonCat();
//        cc.ForkJoinExample();
//
        Fibonacci fi=new Fibonacci(4);
        fi.init();

        //Parallel reducer accumulator
        BiFunction<Integer,Integer,Integer> accumulator = (a,b) -> {
            int c=a+b;
            System.out.printf("accumulator a[%d] | b[%d]:"+c,a,b);
            return c;
        };

        System.out.println(Arrays.asList(1,2)//3,4,5
                .parallelStream()
                //.stream()
                .reduce(0,accumulator,(s1, s2) -> {
                    int c2=s1 + s2;
                    System.out.format("combiner s1 %d | s2 %d:"+c2,s1,s2);
                    return c2;
                }));

        //blocking q: all offer/poll with timeout has checked exception need to declare
//        BlockingDeque<Integer> queue = new LinkedBlockingDeque<>();
//        queue.pollFirst()

    }
}

//Executors invokeAll() vs invokeAny()
class Race {
    static ExecutorService service = Executors.newFixedThreadPool(8);
    public static int sleep(String tag) {
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()+" ["+tag+"]sleep done");
        } catch (Exception e) {}
        return 1;
    }
    public static void hare() {
        try {
            Callable c = () -> sleep("Hare");
            final Collection<Callable<Integer>> r = Arrays.asList(c,c,c);
            List<Future<Integer>> results = service.invokeAll(r);
            System.out.println("Hare won the race!");

            service.shutdown();
            System.out.println("Hare won & shutdown");
            try {
                if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                    System.out.println("Hare won & shutdownNow");
                }
            } catch (InterruptedException ex) {
                System.out.println("Hare won & shutdownNow ex");
                System.out.println(ex);
            }
        } catch (Exception e) {e.printStackTrace();}
    }
    public static void tortoise() {
        try {
            Callable c = () -> sleep("Tortoise");
            final Collection<Callable<Integer>> r = Arrays.asList(c,c,c);
            Integer result = service.invokeAny(r);

            System.out.println("Tortoise won the race!");
            service.shutdown();
            System.out.println("Tortoise won & shutdown");
            try {
                if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                    System.out.println("Tortoise won & shutdownNow");
                }
            } catch (InterruptedException ex) {
                System.out.println("Tortoise won & shutdownNow ex");
                System.out.println(ex);
            }
        } catch (Exception e) {e.printStackTrace();}
    }
    /*
    ConcurrentHashMap : when u want multithreaded index based get/put,
    only index based operations are supported. Get/Put are of O(1)
    ConcurrentHashMap does not guarantee* the runtime of its operations as part of its contract. It also allows tuning
    for certain load factors (roughly, the number of threads concurrently modifying it).

    ConcurrentSkipListMap : More operations than just get/put, like sorted top/bottom
    n items by key, get last entry, fetch/traverse whole map sorted by key etc.
    Complexity is of O(log(n)), So put performance is not as great as ConcurrentHashMap.
     It't an implementation of ConcurrentNavigableMap with SkipList.
     ConcurrentSkipListMap, on the other hand, guarantees average O(log(n)) performance
     on a wide variety of operations. It also does not support tuning for concurrency's
     sake. ConcurrentSkipListMap also has a number of operations that ConcurrentHashMap
     doesn't: ceilingEntry/Key, floorEntry/Key, etc. It also maintains a sort order,
     which would otherwise have to be calculated (at notable expense) if you were using
     a ConcurrentHashMap.

    To summarize use ConcurrentSkipListMap when you want to do more operations on map
    requiring sorted features rather than just simple get and put.
    Basically, different implementations are provided for different use cases.
    If you need quick single key/value pair addition and quick single key lookup,
    use the HashMap. If you need faster in-order traversal,
    and can afford the extra cost for insertion, use the SkipListMap
    * */
    public static void main(String[] p) throws Exception {
        service.execute(() -> hare());
        service.execute(() -> tortoise());
        //ConcurrentSkipListMap //its key are kept sorted -> ConcurrentNavigableMap -> NavigableMap -> SortedMap

        /*
        * */
//        while(!service.awaitTermination(3000,TimeUnit.MICROSECONDS)){
//            service.shutdownNow();
//        }
        //note the running daemon will not terminate bcz service not shutdown yet
    }
    /*ExecutorService execute() vs submit()
    from the JavaDoc execute(Runnable) does not return anything.

    However, submit(Callable<T>) returns a Future object which allows a way
    for you to programatically cancel the running thread later as well as get the
    T that is returned when the Callable completes.

    Future<?> future = executor.submit(longRunningJob);
    ...
    //long running job is taking too long
    future.cancel(true);

    Moreover, if future.get() == null and doesn't throw any exception then
    Runnable executed successfully
    * */
}

//deal lock example
//jconsole  VM :    -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=false
class Riddle {
    public void sleep() { try {Thread.sleep(1000); } catch (Exception e) {} }
    public String getQuestion(Riddle r) {
        synchronized (this){

            sleep();
            System.out.println("getQuestion this["+Integer.toHexString(this.hashCode())+"] => ["+Integer.toHexString(r.hashCode())+"]         ["+Integer.toHexString(Thread.currentThread().hashCode())+"]"+Thread.currentThread().getName());
            if(r != null)
            {
                //notifyAll();
                //yield();

                System.out.println("getQuestion on 1 "+Thread.currentThread().getName());
                r.getAnswer(null);
                System.out.println("getQuestion on 2 "+Thread.currentThread().getName());
            }
            return "How many programmers does it take " + "to change a light bulb?";
        }
    }
    public String getAnswer(Riddle r) {
        synchronized (this) {
            sleep();
            System.out.println("getAnswer this[" + Integer.toHexString(this.hashCode()) + "] => [" + Integer.toHexString(r.hashCode()) + "]       [" + Integer.toHexString(Thread.currentThread().hashCode()) + "]" + Thread.currentThread().getName());
            if (r != null) {
                //notifyAll();
                //yield();
                System.out.println("getAnswer on 1 " + Thread.currentThread().getName());
                //r.getAnswer(null);

                r.getQuestion(null);
                System.out.println("getAnswer on 2 " + Thread.currentThread().getName());

            }
            //r.getQuestion(null);
            return "None, that's a hardware problem";
        }
    }
    /*find deadlock in os process
    linux: ps -ef|grep xxxx
            ls -l
    windosw: jps -l

        jstack 5388


        to solve deadlock , make sure the synchronized obj sequence are the same,
        lock obj1 then lock obj2

        Banker's Algorithm ***
        Requests <= Need & Request <= Available

    * */
    public static void main(String... ununused) {
        final Riddle r1 = new Riddle();
        final Riddle r2 = new Riddle();
        ExecutorService s = Executors.newFixedThreadPool(2);
        Future<String> submit = s.submit(() -> r1.getQuestion(r2));
        Future<String> submit1 = s.submit(() -> r2.getAnswer(r1));
        s.shutdown();

        try {
            System.out.println("submit:"+submit.get());
            System.out.println("submit1:"+submit1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}

//Executor service wait for task complete
class Athlete {
    int stroke = 0;
    public synchronized void swimming() { stroke++; }
    public static void main(String... laps) {
        ExecutorService s = Executors.newFixedThreadPool(10);
        Athlete a = new Athlete();
        for(int i=0; i<1000; i++) {
            s.execute(() -> a.swimming());
        }
        s.shutdown();
        try {
            boolean b = s.awaitTermination(2000, TimeUnit.MICROSECONDS);
            //while (!s.isTerminated());//almost equivalent to awaitTermination
            boolean terminated = s.isTerminated();
            System.out.println("awaitTermination: "+b);
            System.out.println("terminated: "+terminated);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print(a.stroke);
    }
}

//RecursiveAction
class CountSheep extends RecursiveAction {
    static int[] sheep = new int[] {1,2,3,4};
    final int start;
    final int end;
    static int count = 0;
    public CountSheep(int start, int end) {
        this.start = start;

        this.end = end;
    }
    public void compute() {
        if(end-start<2) {
            count+=sheep[start];
            return;
        } else {
            int middle = start + (end-start)/2;
            invokeAll(new CountSheep(start,middle),
                    new CountSheep(middle,end));
        }
    }
    public static void main(String[] night) {
        ForkJoinPool pool = new ForkJoinPool();
        CountSheep action = new CountSheep(0,sheep.length);
        pool.invoke(action);
        pool.shutdown();
        try {
            boolean b = pool.awaitTermination(2000, TimeUnit.MILLISECONDS);
            System.out.println("awaitTermination:"+b);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("awaitQuiescence:"+pool.awaitQuiescence(2000, TimeUnit.MILLISECONDS));
        System.out.print(action.count);
    }
}

//Bank concurent add/minus
class BankConcurrent {
    static int cookies = 0;
    public synchronized void deposit(int amount) {
        cookies += amount;
    }
    //note if synchronized method signature becomes static then
    //it lock on BankConcurrent.class object instead of instance obj
    public synchronized void withdrawal(int amount) {
        cookies -= amount;
    }
    public static void main(String[] amount) throws Exception {
        ExecutorService teller = Executors.newScheduledThreadPool(50);
        BankConcurrent bank = new BankConcurrent();
        for(int i=0; i<25; i++) {
            teller.submit(() -> bank.deposit(5));
            teller.submit(() -> bank.withdrawal(5));
        }
        teller.shutdown();
        teller.awaitTermination(10, TimeUnit.SECONDS);
        System.out.print(bank.cookies);
    }
}


//Chapter 21  OCP JDBC
//java.sql:
    //public class DriverManagerDriverManager
    //interface Driver
    //public interface Connection  extends Wrapper, AutoCloseable

/*
execute() : The method used for all types of SQL statements, and that is, returns a boolean value of TRUE or FALSE. If the method return TRUE, return the ResultSet object and FALSE returns the int value.

executeUpdate() : This method is used for execution of DML statement(INSERT, UPDATE and DELETE) which is return int value, count of the affected rows.

executeQuery() : This method is used to retrieve data from database using SELECT query. This method returns the ResultSet object that returns the data according to the query.
* */
class EmptyTable {
    /*
    com.mysql.jdbc.NotUpdatable: Result Set not updatable.
    See the JDBC 2.1 API Specification, section 5.6 for more details.
    This result set must come from a statement that was created with
    a result set type of ResultSet.CONCUR_UPDATABLE,
    the query must select only one table, can not use functions and
    must select all primary keys from that table.
    * */
    public static void main(String[] args) throws SQLException { // s1
        String url = "jdbc:mysql://localhost:3306/mtravel_db?verifyServerCertificate=false&autoReconnect=true&useSSL=false";
        try (Connection conn = DriverManager.getConnection(url,"root","630716"); // s2
              Statement stmt = conn.createStatement//();
                      (//TYPE_SCROLL_SENSITIVE
                      ResultSet.TYPE_SCROLL_SENSITIVE,//scrollable
                      ResultSet.CONCUR_UPDATABLE);

             /*
             * */
             //stmt.execute() -> boolean [true: ResultSet obj, false: int]
             //stmt.executeUpdate() -> int
             //stmt.executeQuery() -> ResultSet
              ResultSet rs = stmt.executeQuery("select * from dccntry")) {

            conn.setAutoCommit(false);
            rs.absolute(1);
            System.out.println(rs.getString(2));//orignal value
            if (rs.next()) {//throws SQLException;
                rs.absolute(0); rs.absolute(1);

                //note absolute pass in negative is allowed: eg. -1 move to last()
                rs.relative(1);// true if on a valid row, false if not
                //rs.beforeFirst();//void
                //rs.afterLast(); //void Moves the cursor to the end of this ResultSet object, just after the last row. This method has no effect if the result set contains no rows.

                //rs.updateString(2,"AUS1");
                rs.updateRow();
                //the cursor is initially placed b4 1st row ->
                // so calling rs.get... without rs.next() will throw SQLException
                /// moves the cursor forward 1 row from cur position
                System.out.println(rs.getString(2));//throws SQLException;


                //conn.rollback();//Can't call rollback when autocommit=true
                //rs.updateRow();
                //conn.commit();
                //System.out.println(rs.getString(2));//throws SQLException;

            }
        }
    }
}





//Chapter 22  OCP Locale
//Locale.getDefault() to get system default locale,
//Locale.setDefault() to set default

class ListType extends ListResourceBundle{//_US
    protected Object[][] getContents() {
        return new Object[][] {
                { "keys", new ArrayList<String>() }
        };
    }
    public static void main(String[] args) {
        Locale.setDefault(Locale.KOREAN);
        System.out.println(Locale.getDefault());//ko
        Locale.setDefault(new Locale("en", "AU"));
        System.out.println(Locale.getDefault());//en_AU
        Locale.setDefault(new Locale("EN"));
        System.out.println(Locale.getDefault());//en

        ResourceBundle rb = ResourceBundle.getBundle("com.atguigu.springcloud.service.TestService");
        List<String> keys = (List) rb.getObject("keys");

        keys.add("q");
        keys.add("w");

         keys.add("e");
         keys.add("r");
         keys.add("t");
         keys.add("y");
         System.out.println(((List) rb.getObject("keys")).size());
         //this add will modify the Resource object
    }
}


@FunctionalInterface
interface Flora<T>{


    Number fly(Object t);
    default void open(Object t){
        fly(t);
    }

    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { fly(t); after.accept(t); };
    }
}

interface Flo<T> extends Flora<T>{
    Number fly(Object t);
    default void open(Object t){
        System.out.println(t);
    }

//    default Consumer<T> andThen(Consumer<? super T> after) {
//        Objects.requireNonNull(after);
//        return (T t) -> { fly(t); after.accept(t); };
//    }
}

class Cactus implements Flora,Flo{
    //* note if both interface contains same default method,
    // child need to override it or else compile time error

    public void open(Object t){

    }

    @Override
    public Integer fly(Object t) {
        //note that this override return type
        // is Integer which is not same with parent method Number
        // bcz overridden methods must have covariant return types(Number>Integer or List>ArrayList)
        return 0;
    }
}
abstract class Catctip {

}
abstract class Cacti extends Cactus{
   // abstract void kon();
    public void open(Object j){
        return;
    }
    public Integer fly(Object t) {
        //new Catctip(); cannot instantiate abstract class
        //Cactus ci=new Cacti();
        //abstract class cannot be instantiated even it has no abstract method
        return new Integer(1);
    }
}



interface Organism{//interface var is implicitly public static final
    final int live=0;//final
    public final static int blood=10;
    //public final static is default=>
    // so it must assign a value at declare time

//    static{
//        //static initialization block not allow in interface
//    }
//    {
//        initialization block not allow in interface
//    }

    void talk();

    default void open(){

    }
    default void opens(){

    }
    //static void meh();//static methods in interface should have a body
    <T,K,V,B> T live(T obj,K k,V v,B a);
    public int go();//only public allow in interface method
    //abstract method cannot be final in both interface & abstract class
    static void see(){//static cannot be final in interface
        //interface can have static method=> static method must got body
        //
        System.out.println("Organism see see");
    }
}

abstract class Reptiles implements Organism{

    //public abstract void talk();

    @Override
    public <T, K, V, B> T live(T obj, K k, V v, B a) {
        return null;
    }

    @Override
    public int go() {
        return 0;
    }
}


class Animal<T extends Object & Organism>{
    //interface must be at the end
    T obj;
    public Animal(T obj_){

        System.out.println("Animal obj_ :"+obj_);
        obj=obj_;
        //obj.live("ok",1,2.2,true);
    }
    public Animal(){
        if(null!=obj)obj.live("ko",2,'a',false);
        System.out.println("Animal emoty cstr");
    }
}


class Football {
    public static Long getScore(Long timeRemaining) {// m1
        if(timeRemaining instanceof Number==false ||
        !Long.TYPE.isInstance(timeRemaining)){
            return null;
        }
        return 2*timeRemaining;
    }
    final static void $test(){

    }
    public static Integer getScore(Integer timeRemaining) {// m3
        return 2*timeRemaining;
    }
    public static void main(String[] refs) {
        final int startTime = 4;
        //cannot implicitly convert primitive int to wrapper Long
        //note if primitive int to wrapper Integer is acceptable
        //Integer is not subclass of Long, so cannot pass into m1
        System.out.print(getScore(startTime)); // m2
        //m2 choose m3 instead of m1, if m3 is not presents, compile error occurs

        try {
            System.out.print('A');
            throw new RuntimeException("Out of bounds!");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.out.print('B');
            throw aioobe;
        }
        finally { System.out.print('C'); }
    }
}


//inheritance example 1*
class Automobile {
    public Automobile(int v){
        this.speed=v;
    }
    int speed=0;
    public void test(int j){}
    private final String drive() { return "Driving vehicle"; }
}
class Auto extends Automobile {
    int speed=1;
    private final Object contents;//vairable 'contents' might not have been initialized
    public Auto(int v) {
        super(v);
        this.contents=v;
    }

    protected String drive() throws RuntimeException {
        super.test(1);
        return "Driving car";
        //System.out.println();//unreachable statement
    }
    //cannot override final method// unless it is private(bcz not visible to child)
}
class ElectricCar extends Auto {
    int speed=10;
    @Override
    public final String drive() throws ArithmeticException {
        return "Driving electric car";
    }//override
    public ElectricCar(int h){
        this();
    }
    public ElectricCar(){
        super(1);
    }
    static Auto carToy;
    public void testfunc(Auto car){
        Object car1o = new ElectricCar();
        Auto car1 = new ElectricCar();
        System.out.println("testfunc:"+car1);
        carToy=car1;
    }

    static char sbchar;
    public static void main(String[] wheels) {

        String strg="hello ";
        final Integer ji=10;
        final Auto car = new ElectricCar();
        final Automobile auto = new Auto(1);
        final Automobile autoE = new ElectricCar();
        final ElectricCar eCar = new ElectricCar();
        final Object eCo = new ElectricCar();
        System.out.println("car speed :"+strg+ji+car.speed);//1
        System.out.println("auto speed :"+auto.speed);//0
        System.out.println("autoE speed :"+autoE.speed);//0
        System.out.println("eCar speed :"+eCar.speed);//10


        System.out.println("default value of char :"+sbchar);//
        //sbchar='\u0000';//null
        System.out.println("default value of equality char[\u0000]:"+(sbchar=='\u0000'));//
        System.out.println("default value of equality char[\u0000]:"+(sbchar=='\u0020'));//
        boolean[] barr=new boolean[10];
        Boolean[] barr1=new Boolean[10];
        System.out.println("boolean array:"+(barr));//[Z@6d21714c
        System.out.println("Boolean array:"+(barr1));//[Ljava.lang.Boolean;@108c4c35
        System.out.println(Arrays.toString(barr));//[false, false, false, false, false, false, false, false, false, false]
        System.out.println(Arrays.toString(barr1));//[null, null, null, null, null, null, null, null, null, null]

        StringBuilder bs=new StringBuilder("oksb");
        System.out.println(bs);
        bs.delete(0,1000000);

        //java.time.LocalDate ld = new java.time.LocalDate(2022,6,30);
        //LocalDate has private access -> cannot be instantiated
        LocalDate ofdate = LocalDate.of(2022, 6, 30);
        LocalTime oftime = LocalTime.of(23,50,45,888_888_888);
        java.time.LocalDateTime ofdatetime = java.time.LocalDateTime.of(2022,6,30,23,30,45,888_888_888);
        ofdatetime= java.time.LocalDateTime.of(ofdate,oftime);




        //System.out.println("eCo speed :"+eCo.speed);//compile time error
        //note the variable does not apply to polymorphism,
        // it remains ref to the declared type

        System.out.println();

        System.out.println(carToy);
        ElectricCar carb = new ElectricCar();
        carb.testfunc(carb);
        System.out.println(carToy);

        System.out.print(car.drive());
    }
}


// class Big {
//    public Big(boolean stillIn) { super(); }
//}
//class Trouble extends Big {
//    public Trouble() {}
//    public Trouble(int deep) { super(false); this(); }
//    public Trouble(String now, int... deep) { this(3); }
//    public Trouble(long deep) { this("check",deep); }
//    public Trouble(double test) { super(test>5 ? true : false); }
//}

//overload:
//      same method name, overload must have diff params(types differs or no. of param differs),
//      return type/access modifier doesnt matter

//override/inherited method:
//      - method must have exact same parameters(same type, covariant also cannot)
//      - return type can be covariant(same or sub-type of parent)
//      - overridden method declared checked exception cannot be new or broader exception from parent method(it can omit or sub-class of the declared exception)
//      - overridden method access modifier can only be same or broader
// there is no static method override, it it exists then its 2 diff method with same name and params in 2 classes
class ChooseGrandParent{

}
class ChooseWisely1 extends ChooseGrandParent{
    public ChooseWisely1(){

    }
    public ChooseWisely1(int a) {
        this();
    }
}
//same method name & same parameter cannot exist in same class
class ChooseWisely extends ChooseParent{
    public ChooseWisely(){
        super(0);
    }
    public ChooseWisely(int a) {
        super(a);//since ChooseParent has no constructor ,
        //so this constructor and super is a must, else compile time error : "There is no default constructor available in ChooseParent...."
        String name = "Desiree";
        int _number = 694;
        boolean profit$$$; //might not have been initialized
        //System.out.println(name + " won. " + _number + " profit? " + profit$$$);
    }
    @Override
    Integer choose(Number choice) { return 5; }//Override base, covariant
    //public Object choose(int choice) { return 5; }//overload base

    int choose(Integer choice) { return 5; }//overload base

    public Object choose(int choice,String ok) { return (++choice)+5; } //overload 1
    //public Exception choose(int choice,String ok) { return new RuntimeException(); } //overload 1
    public String choose(short choice) { return "2"; }//overload 2
    private int choose(CharSequence choice) { return 11; }//overload 3
    private int choose(String choice) { return 11; }//overload 4
    private int choose(long choice) { return 11; }//overload 4
    private void choose(double choice) { return; }//overload 4
    private int choose(int choice) { return root+11; }//overload 4
    static void testLong(long lval)throws IOException{
        System.out.println(1/0);
        lval = 1^ 7;
    }

    //@Override
    public static void choose(char choice) { return; }//overload 3
    public static void choose(Object choice) { return; }//overload 3
    public static void main(String[] path)   {
        Integer ja=10;
        Long jaa=100l;
//        try{
//            testLong(ja);
//        }catch (IOException ec){
//            System.out.println("throw io:"+ec.getMessage());
//        }
        System.out.println("testLong passed");

        System.out.print(new ChooseWisely(1).choose(((long)2)+1));
        System.out.print(new ChooseWisely(2).choose(2+1));//default int
        System.out.print(new ChooseWisely(3).choose(((byte)2)));
        if(2==2)
        if(1==2)
        if(2==2)
        System.out.println("end");

        else
        System.out.println("end2");
        System.out.println("end3");
        //*Integral data type default is int
        //here(byte) only converts 2, then 2+1 => becomes int again
        //bcz + operator automatically promotes any byte/short to int
        //if only (byte) is provided then it will run => choose(short choice) , as its the closest datatype matched
    }
}
class ChooseParent{
     static int root;//final static can only be set in static block, cannot be in constructor
    static {
        root=0;
    }
    {
        {
            //obj=null;
            obj=new Object();
        }
    }
    protected int cal=-1;
    private final Object obj;
    protected final int kj=10;
    final protected int jk=99;
    public ChooseParent(int a){
        //obj=new Object();
        root=a;
    }
    public static void choose(char choice) { return; }//overload 3
    Number choose(Number choice) {

        super.hashCode();
        System.out.println("root b4 --> "+root );//1
        this.root=0;
        System.out.println("root mid--> "+root );//0
        ChooseParent.root=(int)choice;
        System.out.println("root after--> "+root );//10
        return root;
    }//overload base


    public static void main(String[] args) throws IOException{

        ChooseParent cp = new ChooseParent(1);
        cp.choose(10);
        System.out.println("jk  --> "+ cp.jk );//99
        System.out.println("kj  --> "+ cp.kj );//10

    }
}


//*** inheritance example: constructor start***
class RainForest extends Forest {
    public RainForest(){
        super(1);
    }
    public RainForest(int ak){
        //super(ak);
        this();
        //ChooseParent.root=1;
    }
    static RainForest sc = new RainForest();
    RainForest asv(){
        return sc;
    }
    void $___(){}

    public RainForest(long treeCount) {
        super(treeCount);//if without this line, the code will not compile
        //bcz parent class "Forest" has no empty constructor ,
        //if here do not call super(), compiler will automatically insert
        //an empty super() constructor , but parent class only has parameter-constructor
        //so the code will has compiling error
        this.treeCount = treeCount+1;
    }
    public static void main(String[] birds) {

        try{
            main(birds);
        }catch (Exception ec){
            System.out.println("Exception:"+ec);
        }
        catch (Throwable ec){
            System.out.println("Throwable:"+ec);
        }

        Forest frst= new Forest(1);
        RainForest rainfrst;//=(RainForest) frst;//java.lang.ClassCastException

         frst= new RainForest(1);
         rainfrst=(RainForest) frst;//cast with no issue

        System.out.print(new RainForest(5).treeCount);
    }
}
class Forest {
    public long treeCount;
    public Forest(long treeCount) {
        this.treeCount = treeCount+2;
    }
}
//*** inheritance example: constructor end***

//pass by value example
class Ski {
    public int age = 18;
    protected int[] arri=new int[2];
    public Integer play(int num){
        System.out.println(num);
        return num;
    }
//    public void play(int num){//same name same params not allow (even u change return type)
//        System.out.println(num);
//    }
    public Long play(int num,int num2){//overload can use different return type only when params differs
        System.out.println(num);
        return new Long(num);
    }
    public static void slalom(Ski racer, int[] speed, String name,int[] arr) {
        //racer=new Ski();
        racer.age = 18;
        name = "Wendy";//we can reassign it even if String name is final passed in
        //speed = new int[1];//note that even if speed is final passed in , but it just acopy we still change reassign
        speed[0] = 11;
        racer = null;

        //arr = new int[1];//if this line is added then it reassigned the arr reference to another obj in heap
        // so it will not change the original array obj val
        arr[0] = 100;
        return;//method with void can still call return
    }
    public static void main(String... mountain) {
        final Ski mySkier = new Ski();
        mySkier.age = 16;
        mySkier.arri=new int[3];
        mySkier.arri[0]=1;
        final int[] mySpeed = new int[1];mySpeed[0]=99;
        final String myName = "Rosie";
        slalom(mySkier,mySpeed,myName,mySkier.arri);
        System.out.println(mySkier.age);//18
        System.out.println(myName);//Rosie
        System.out.println(Arrays.toString(mySpeed));//[11]
        System.out.println(Arrays.toString(mySkier.arri));//[100,0,0]
    }
}
class Playground extends Animal implements Organism {


    @Override
    public void talk() {

    }

    @Override
    public <T,K,V,A> T live(T obj,K k,V v,A a) {
        System.out.println("living obj:"+obj);
        System.out.println("living k:"+k);
        System.out.println("living v:"+v);
        System.out.println("living a:"+a);
        return obj;
    }

    @Override
    public int go() {
        return 0;
    }

    public interface play{

    }

    static Playground pgl;
    private int oa;


    static {
        pgl=new Playground();
    }
    {

        System.out.println("b4 cstr");
    }
    public Playground(){
        //super(new Animal<>());
        System.out.println("cstr");
        end = 1;
        //****final instance variable must initialize in all constructor
    }
    static char c;



    static int start = 2;
    final int end;//u can only assign final variables in constructor,
    //// if multiple constructors are present, must assign final var
    // to its val in all the constructors

//    void setEnd(int n){
//        this.end=n;//setter not allow ser final var
//    }

    static int ok;
    private int pint=1;
    private void callPint(){
        System.out.println(pint);
    }
    public Playground(int x) {
        this();//must be 1st line
        //super();
        int testlocal;
        System.out.printf("maintestok"+ok);//instance & class variable default to its default val base on type
        //**where local variable doesnt have a default variable, err msg: [variable ... might not have been initialized]


        long g=new Integer(3);
        System.out.printf("", g);//primitive type has no method to call
        //int num1,double num2=0;//invalid
        int num1,num2;
        int num3,num4=0;
        int num5=9,num6=8;
        System.out.println( "Playground x1 "+x);
        x = 4;
        System.out.println( "Playground x2 "+x);
        //System.out.println( "Playground endb4 "+end);
        //end = x;//if this cstr calls empty cstr, and empty cstr oredy set the var,
        // then here cannot set the var again, or else cannot compile
        System.out.println( "Playground x3 "+x);
        System.out.println( "Playground end "+end);
    }
    public void fly(int distance) {
        System.out.println( "Playground fly "+distance);
        System.out.println( "Playground end "+end);
        System.out.println( "Playground start "+start);
        System.out.println(end-start+" ");
        System.out.println(distance);
    }

    static  int [][]zza;//new int[2][];

    public static String runTest(boolean spinner, boolean roller,
                                 String... args//varargs must be last and must be at last
                                 //,String... args
    ) {
        //main("1","2","3");

        if(spinner = roller) return "up";
        else return roller ? "down" : "middle";


    }

    static int[][] game = new int[6][6];
    static Object[] objgn=new Object[2][];
    static Object objgo=new Object[2][][];

    public static void testMe(){
        return;
    }
    public static void voidMethod(){}//<K,V>
    public static void main(String... args) {

        Stream < Integer > numsa = Stream.of(1, 2, 3, 4, 5);
        Stream<Integer> integerStream = numsa.filter(n -> n % 2 == 1);//stream can only be consumed once
        integerStream.forEach(p -> System.out.println("===okok>>>>"+p));

        class Invoice {
            void print() {System.out.println("Invoice Printed");}
        }
        int a12 = 2;
        boolean a12b = false;
        a12b = a12++ == 2 || --a12 == 2 && --a12 == 2;//&& has higher precedence over || , postfix higher than prefix => short circuit
        System.out.println("a12b => "+a12b);//true

        //Playground ps = new Fish();
        //args[0]="";
        //System.out.println("args[0]"+args[0]);//java.lang.ArrayIndexOutOfBoundsException
        //System.out.println("args[1]"+args[1]);
        int[] nums[] = new int[][] { { 0, 0 } };
        int[] nums2[][] = new int[2][][];
        int[] num1 = (int[])nums[0];
        nums2= new int[][][] {};

        int[] nums3 = new int[2];

        asd:{
            System.out.println("asd block breaking now");
            break asd;
        }
        //take note:
        int[] arr1a[],arr2a;//arr1a is 2d, arr2a is 1d
        int[] []arr1,arr2mm;//both 2d array
        //int[] []arr3,[]arr4;// compilation error, [] only valid for 1st var

        //int[1] ass;//declaration cannot define size, else compilation error
//        game[3][3]=1;
//        Object[] obja=game;
//        game[3][3]="x";
        //Animal<Playground> mc=new Animal<>(new Playground());

        //System.out.println(voidMethod());//void method cannot be printed
        int count = 0;
        do {

            do {
                count++;
            } while (count < 2);//do while execute until while evaluated to false
                break;
        } while (false);
        System.out.println(count);

        int[] taxis= new int[]{'A','a','0'};
        for (Object obj : taxis) {
            System.out.println((int)obj);
        }
        //List<> alm3 = new ArrayList();
        List<String> alm1 = new ArrayList<>();
        //List<> alm2 = new ArrayList();//diamond sign cannot be at left(can only reside on instantiating side)

        int kj=10;
        int j_k=+-+(kj--);

        numb:for(;++kj<14;){
            numb1:for(;kj++<15;){
                System.out.println("kj===> "+kj);
                continue numb;
            }
            break numb;
        };//optional semi-colon

        for(;++kj<11;);//no body for loop

        whloop:while(++kj<11)
        {
            break whloop;
        };
        System.out.println("kj===> "+kj);

        whloop:while(++kj<11);//whloop can has duplicate, bcz context different
        whloop:{
            System.out.println("after whloop 2 block in kj:"+kj);
            if(kj>11){
                break whloop;
            }
            System.out.println("after whloop 2 block in end");
        }
        { System.out.println("after whloop 2 block in a");};
        { System.out.println("after whloop 2 block in b");};
        { System.out.println("after whloop 2 block in c");};
        String race = "";

        //1.test loop start
        loop:
        do {
            race += "x";
            break loop;//will be undefined if remove the do while loop
        } while (true);
        System.out.println(race);
        //test loop end




        ArrayList<Object> arrlist=new ArrayList<>();
        arrlist.add(new String());
//        String name = "Desiree";
//        int _number = 694; boolean profit$$$;
//        System.out.println(name + " won. " + _number + " profit? " + profit$$$);

        final Stream<Object> streamList =
                StreamSupport.stream(arrlist.spliterator(), false);


        objgn=game;//legal but dangerous
        objgo=game;//legal but dangerous
        System.out.println("test arr -->>> game:"+Arrays.toString(game));
        System.out.println("test arr -->>> objgn:"+Arrays.toString(objgn));
        System.out.println("test arr -->>> objgo:"+Arrays.toString((Object[])objgo));
        //game[3][3] = 6;
        //game[3]="";
        Object[] obj = game;//legal but dangerous
        //obj[3] = "X";//ArrayStoreException
        eachl:for (Object oj: obj) {
            System.out.println("test arr -->>> game:"+Arrays.toString((int[])oj));
        }

        for(obj = game;game.length<0;obj = game){
            break;
        }

        String version = System.getProperty("java.version");
        String testn= null;
        if(testn instanceof String){//null is not instanceof String
            System.out.println("testn instanceof String true");
        }else{
            System.out.println("testn instanceof String false");
        }
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        long totalMemory =Runtime.getRuntime().totalMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();

        System.out.println(runTest(false,true));

        String[] sparr=new String[]{"A","cat","a","b","dog","Damn"};
        // Arrays.sort(sparr);
        System.out.println("sparr :"+Arrays.toString(sparr));
        Arrays.sort(sparr);
        System.out.println("sparr :"+Arrays.toString(sparr));
        //binarySearch must be sorted according to natural order, else result is unpredictable
        int isearch=Arrays.binarySearch(sparr,"bd");//returns -5 means not found
        //this binarySearch index logic is as below:
        // where it should insert if it is exists in current sorted sequence
        // for "bd" in [A, Damn, a, b, cat, dog] should be inserted at index: 4
        //-(low + 1)
        //so isearch = -(4+1) = -4 -1 =-5
        System.out.println("isearch :"+isearch);

        int[][][] nums2a[], nums2b;

//        for (int[][] ints : nums2b) {
//
//        }
//        StringBuffer sb_=new StringBuffer("a");
//        sb_.capacity();sb_.length();

        //var stvar=1;//var available in java 10 onwards
        //System.out.println(" stvar :"+stvar);

        float hj=12;
        //float hj1=1.2;//provided double, *** floating point default is double


        //test num switch
        int num = 0;
        switch (num) {
            default:
                System.out.println("num switch default");
            case 0:
                System.out.println("num switch case 0");
            case 1:
                System.out.println("num switch case 1");
                break;
        }

        short colorOfRainbow=3;
        final int red = 5;//must be constant
        switch(colorOfRainbow){
            //System.out.println("ok");
        }
        switch(colorOfRainbow) {

            default:
                System.out.print("colorOfRainbowHome");
                //break;
//            case 2147483647://must be covariant type of colorOfRainbow
//                System.out.print("2147483647");
            case 10/3:
                System.out.print("colorOfRainbow 10/3");
            case red://must be constant
                System.out.print("Away");
        };

        char aswi=65;
        switch(aswi){//switch not accept floating point and long
            case 'a':
                System.out.println('a');
                break;
            default:
                System.out.println("aswi:"+aswi);
                //break;
            case 'A':
                System.out.println("char 65 -> A");
                break;
        }

//        boolean nn=true;//now allowed //enum only allowed after java 6, String only allowed after java 7
//        switch (nn){
//
//        }



        //test switch case
        final int flavors = 10;
        int eaten = 0;
        switch(flavors) {
            //***switch case if no break.
            // all cases that is after the match case will be executed(follow the declared sequence)
            //no matter it matches the case or not
            case 30:
                eaten++;
                System.out.println("flavors case 30 :"+eaten);
                //break;
            default:
                eaten--;
                System.out.println("flavors case default :"+eaten);
                //break;
            case 40:
                eaten+=2;
                System.out.println("flavors case 40 :"+eaten);
                //break;
            case 50:
                eaten+=1;
                System.out.println("flavors case 50 :"+eaten);
                //break;


        }
        System.out.print(eaten);

        final long winter = 10; //long cannot be a case in switch
        final byte season = 2;
        int fall = 4;//switch case must be constant
        final short summer = 3;
        switch(season) {
            case 1:
            //case winter: System.out.print("winter");
            //default:
            //case fall: System.out.print("fall");
            case summer: System.out.print("summer");
            default:
        }


        Object[] tdstring=new String[3];
        //tdstring[0]=new Object();//runtime err: java.lang.ArrayStoreException
        //tdstring[1]=new Double("2.2");//runtime err: java.lang.ArrayStoreException
        //tdstring[1]=new StringBuffer();
        //java.lang.ArrayStoreException
        //compiler only look at reference type variable[tdstring],
        //but jvm runtime will look at the runtime object new String[3]
        //

        byte[] []abyte,bbyte[];//different size array in single line
        abyte=new byte[2][];
        bbyte= new byte[3][][];

        //byte[] bn=new byte[-1];//exception NegativeArraySizeException(RuntimeException)
        byte  tdbyte[]=new byte[1];

        //String  tdbyteBIG[]=new String[2147483648];//2147483648 > 2147483647  4byte max size of int: integer number too large// or OOM
        //byte  tdbytel[]=new byte[10l];//10l is long, need explicit conversion : (int)10l, else compile error
        byte  tdbytea[]=new byte['a'];// 'a'  -> 97
        //byte / short / char -> int (implicit convert)

        short [] tdshort=new short[1];
        int [] tdint=new int[1];
        //Arrays.binarySearch(tdint,1);
        long [] tdlong=new long[1];
        float [] tdfloat=new float[1];
        double [] tddouble=new double[1];
        char [] tdchar=new char[1];
        boolean [] tdboolean=new boolean[1];
        System.out.println("tdstring:"+tdstring);//tdstring:[Ljava.lang.String;@6ff3c5b5
        System.out.printf("tdbyte: %s \n",tdbyte);//tdbyte: [B@3cd1a2f1
        // Byte [] tdbyte=new Byte[1]; => tdbyte:[Ljava.lang.Byte;@3764951d
        System.out.println("tdshort:"+tdshort);//tdshort:[S@49476842
        //  Short [] tdbyte=new Short[1]; => tdshort:[Ljava.lang.Short;@4b1210ee
        System.out.println("tdint:"+tdint);//tdint:[I@78308db1
        // Integer [] tdlong=new Integer[1]; [Ljava.lang.Integer;@78308db1
        System.out.println("tdlong:"+tdlong);//tdlong:[J@27c170f0
        // Long [] tdlong=new Long[1]; => [Ljava.lang.Long;@27c170f0
        System.out.println("tdfloat:"+tdfloat);//tdfloat:[F@5451c3a8
        //Float [] tdlong=new Float[1]; => tdfloat:[Ljava.lang.Float;@7440e464
        System.out.println("tddouble:"+tddouble);//tddouble:[D@2626b418
        //Double [] tdlong=new Double[1]; => tddouble:[Ljava.lang.Double;@78308db1
        System.out.println("tdchar:"+tdchar);//tdchar:[C@5a07e868
        //Character [] tdlong=new Character[1]; => tdchar:[Ljava.lang.Character;@27c170f0
        System.out.println("tdboolean:"+tdboolean);//tdboolean:[Z@76ed5528
        // Boolean [] tdlong=new Boolean[1]; => tdboolean:[Ljava.lang.Boolean;@2626b418


        int ing= Integer.parseInt("1");
        Integer ingWrapper= Integer.valueOf("2");

        //finalvi=1;
        //int locali=null;//primitive cannot set to null
        new Playground(10).fly(5);
        new Playground(10).pint=1;
        System.out.println(new Playground(10).pint);


        int[] arrij={1,2,3};
        int[] arrij5={1,2,3,4,5};
//        arrij=arrij5;//invalid at runtime
//        arrij5=arrij;//valid
        char [] charaa={'a',91,1,'A'};
        //int[] ari=charaa;//invalid , char can implicitly convert to int, but char[] cannot convert to int[] implicitly
        //[C cannot convert to [I


        play[] parr=new play[2];//array of interface

        Number[] narr=new Number[4];//abstract type array is acceptable
        narr[0]=new Integer(2);
        narr[1]=new Double("0.1");
        Number[] narri=new Integer[4];

        int bch='a'; System.out.println(bch);
        int[][] afai={{1,3},{1,2,3}};
        //afai[0]=null;//valid
        System.out.println(Arrays.toString(afai)+afai.length);
        int[] afa={1,2,3};
        //afa[0]=null;//invalid
        System.out.println(afa);
        System.out.println(Arrays.toString(afa));
        int[] afa1=new int[]{1,2,3};
        System.out.println(afa1.equals(afa));

        int[][] aia=new int[3][];

        System.out.println(Arrays.toString(aia));
        System.out.println(Arrays.toString(aia[0]));
        //System.out.println(aia[1][0]);
        System.out.println(Arrays.toString(aia[2]));


        pgl=new Playground();System.out.println(pgl);
        pgl=new Playground();
        Playground pgn=new Playground();
        pgn.start=2;
        System.out.println(pgl);

        Deque<Integer> deque = new ArrayDeque<>();
        //deque.push(null);
        deque.push(1);//1 is the top
        deque.push(2);//2 is the top
        Iterator<Integer> iterator = deque.iterator();
        while (iterator.hasNext()){
            Integer a=iterator.next();
            //deque.remove(a);
            if(a==2){
                deque.remove(a);//iterator.remove();
            }

        }
        System.out.println(deque);





        System.out.println(args.getClass().getName());
        System.out.println(args.length);
        System.out.println(args[0]);

        long[] arf=new long[2000];
        arf['a'-1]=100; System.out.println('a'-1);
        Playground pg=new Playground();


        //System.out.println(zza.length);

        int arr2[][]=new int[2][];
        arr2[0]=new int[2];arr2[0][0]=1;

        arr2[1]=new int[3];
        arr2[1][0]=2;
        arr2[1][1]=2;
        arr2[1][2]=2;

        System.out.println(arr2.length);
        System.out.println(arr2.getClass().getName());//=> [[I
        int testi=130;
        byte byt1=(byte)testi;
        System.out.println(byt1);


        char sp=197;
        System.out.println(sp);

        char c1 = '\u0000';
        System.out.println(c1);
        System.out.println(c);
        System.out.println(c == c1);

        int octal_i=076;//62
        int dt=2_________77;  System.out.println(dt);
        double hexa_i=0xFace; double ed=1.2e2;  System.out.println(ed);
        int hexa_i2=0Xbeef;
        System.out.println(octal_i+","+hexa_i+","+hexa_i2);
        System.out.println(hexa_i);
        System.out.println(hexa_i2);

        float ft=Float.MAX_VALUE;//.1234567890;
        System.out.println("Hello Ja"+sp+"va"+ ft);
    }
}

//public interface ifg{
//
//}

class BubbleException extends Exception {}
class Fish {
    Fish getFish() throws BubbleException {
        throw new RuntimeException("fish!");
    }
}
final class Clownfish extends Fish {
    public final Clownfish getFish() {
        throw new RuntimeException("clown!");
    }
    @SneakyThrows
    public static void main(String[] bubbles) {
        final Fish f = new Clownfish();
        f.getFish();
        System.out.println("swim!");
    }
}

 class PrintNegative {
    public static void main(String[] args) {
        List<Integer> list= new ArrayList<>();
        list.add(-5); list.add(0); list.add(5);
        print(list, e -> e < 0);
    }
    public static void print(List<Integer> list, Predicate<Integer> p) {
        for (Integer num : list)
            if (p.test(num))
                System.out.println(num);
    }
}

@Service
public class TestService  extends ListResourceBundle{//_US


    protected Object[][] getContents() {
        return new Object[][] {
                { "keys", new ArrayList<String>() }
        };
    }

    final static int ji;
    final private int jie;
    static{
        ji=10;
    }

//    {
//        jie=10;
//    }
    public TestService(){

        double n = new Double(1.2);
        boolean bb= new Boolean(true);
        Boolean nb = true;
        jie=10;
        //ji=10;
        //Playground playground = new Playground(10);
        //this.TestService.genericInvokeMethod(playground,"callPint");//not valid

    }

    char x1=5;//|
    char x=6;//-
    int z='a';//97
    public abstract  class Whale{
        private int height=180;
        protected int weight=80;
        protected abstract  void dive();
        public native  void ne();
        //public static void nes();
    }
    public static char c=9;
    public class Orca extends Whale{
        int depth;


        int $aint=10;
        ArrayList lal=new ArrayList();


        @Override
        public void ne() {
            super.ne();
        }

        @Override
        public void dive( ){

            Number[] oarr=new Number[2];
            Number numobj=new Integer(9);

            char sp=9;

            char c1 = '\u0000';//default null
            System.out.println(c1);
            System.out.println(c);
            System.out.println(c == c1);

            //array
            int[][] arr1[],ar1;//arr1 is 3-dim, ar1 is 2-dim
            //java.lang.NegativeArraySizeException
            int[][] []az1,azz1;//az1 & azz1 are both 3-dim

            //int[][] aa1[],[]aa2;//not valid=> 2nd var cannot declare []

            long size=2;//2147483648 integer number too large => int literal must be in int range
            int [][]arr2=new int[(int)size][];
//            Arrays.stream(arr2).count()
            java.util.List<Integer> li=new ArrayList<>();
            //String ssv="s";ssv.length();

            System.out.println(arr2.getClass().getName());//=> [[I
            System.out.println(arr2.length);
            int arr3[][]=new int[1][1];
            int[] []arr4=new int[1][];
            int[] arr5[]=new int[1][];
            int []arr6[]=new int[2][];
            int[][] arr7[]=new int[3][][];

            //conversion
            float af=123.456f;
            double bf=123.456;
            long cl=123l;
            af=(long)cl;//**valid
            //long can cast to float->double

            double dt=1_23.7;//jdk 1.7 onwards, improve readability
            float fa=1.3e2f;
            float f=12.23F;
            Double d=0786.0;
            double d1=0X223;//floating literal only in decimal form
            double de=1.2e3;
            //double dinvalid=0X223.2;//floating literal only in decimal form
            int octal_i=076;//62
            int hexa_i=0xFace;//64206
            int hexa_i2=0Xbeef;//48879
            int binary_i3=0b1111;
            //int li=10l;
            byte bi=127;short bi2=32767;
            int akmax=2147483647;
            long ln=10;

            System.out.println(super.weight);
            int[][] arrint=new int[2][];
            arrint[0]=new int[2];
            arrint[1]=new int[2];
            String s="a";
            s.length();
            lal.size();
            System.out.println(arrint.length);


            System.out.println("depth"+depth);
        }
    }

    public static Object genericInvokeMethod(Object obj, String methodName,
                                             Object... params) {
        int paramCount = params.length;
        java.lang.reflect.Method method;
        Object requiredObj = null;
        Class<?>[] classArray = new Class<?>[paramCount];
        for (int i = 0; i < paramCount; i++) {
            classArray[i] = params[i].getClass();
        }
        try {
            method = obj.getClass().getDeclaredMethod(methodName, classArray);
            method.setAccessible(true);
            requiredObj = method.invoke(obj, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return requiredObj;
    }


    public static final void main(String[] args) {

        //test arraylist declaration
        ArrayList<String> arl=new ArrayList<>();
        ArrayList arls=new ArrayList<String>();
        ArrayList<String> arl1=new ArrayList();
        ArrayList arl2=new ArrayList();
        ArrayList arl3=new ArrayList<>();
        List arl4=new ArrayList<String>();
        arl.remove(new String("a"));
        arls.add(0,"0 index pos test");
        arls.add(2);
        arls.set(0,"beginning");
        arls.add(1,"5");
        arls.add("str");arls.add(new Object());
        arls.forEach(c->{
            System.out.println("test list obj -> "+c);
        });

        boolean bprimitive=Boolean.parseBoolean("true");//logic is equalsIgnoreCase with "true" is true, all others are false
        Boolean bwrapper= Boolean.valueOf("");//valueOf also internally use parseBoolean

        //for(;;);//unreachable code below : CompileError
        String str_="";

        boolean bo = true^false;//true
        boolean bo1 = true^true;//false
        System.out.println("bo0:"+bo);//true
        System.out.println("bo1:"+bo1);//false
        int kj=10^1;// 1010 XOR 0001  =  1011  -> 11
        int j_k=1+-+-(kj--);
        System.out.println(j_k+""+0+" -j_k-> ");//120
        System.out.println(0+kj);//10

        Playground playground = new Playground(10);
        genericInvokeMethod(playground,"callPint");

        //new Playground(10).pint=1;// cannot accee

    }


    @Resource
    private RestTemplate restTemplate;

    public void startTest() throws JSONException, ArrayIndexOutOfBoundsException
    ,IOException{

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first","jinjian");
        jsonObject.put("second","aaaaaaa");

        long start = System.currentTimeMillis();
        //{1} indicates the first placeholder, or name, but this is another getForEntity overload method
        //TestResponseEntity is a custom dto
//        ResponseEntity<TestResponseEntity> entity = restTemplate.getForEntity("http://39.107.104.221/api/test/{1}", TestResponseEntity.class, 123);
//        long end = System.currentTimeMillis();
//        long cost = end - start;
//        System.out.println("Time consuming:"+cost);
//        RuiooResponseEntity body = entity.getBody();
//        body.getData();
//        body.getStatus();
//        body.getMessage();
        //System.out.println("Response body:"+ body);
    }
}

class StringTest{
    public static final String str_const="you cannot change me";
    public static void main(String[] args) {
        String s1 = new String("you cannot change me");
        String s2 = new String("you cannot change me");
        System.out.println(s1==s2);//false
        String s3 = "you cannot change me";
        System.out.println(s1==s3);//false
        String s4 = "you cannot change me";
        String s5 = "you cannot"+" change me";
        System.out.println(s4==s5);//true
        String s6 = "you cannot";//is not constant
        String s7 = s6+" change me";
        System.out.println(s4==s7);//false
        System.out.println(s4==s7.intern());//true
        final String s8 = "you cannot";
        String s9 = s8+" change me";
        System.out.println(s4==s9);//true

        String strObj=new String("123");
        //strObj.ap

        //0 -> -1
        //1 -> -2
        //2 -> -3
        //100 -> -101

        // -(x+1) = ~x
        int hj0=100;
        System.out.println(~hj0);
        int[] arri=new int[]{1};
        char[] carri=new char[]{0,1,2,7,48,55,65,97,99,100,65535};//range : 0-65535
        //if(arri==carri);//compile time error
        for (char c : carri) {
            System.out.println("cc["+(int)c+"]: "+c);
        }

        char c7='7';// default
        char c71=7;// luck 7
        Integer iw7=7;//luck 7
        int i7=7;//luck 7
        if(i7>10){

        }
        Character cw7='7';//default
        Character cwi7=7;//luck 7
        System.out.println(c7==cw7);
        System.out.println("c7:"+(int)c7);//55
        System.out.println("cw7:"+(int)cw7);//55
        System.out.println((int)c7==(int)cw7);
        switch(cwi7){
            case 7:
                System.out.println("luck 7");
                break;
            default:
                System.out.println("default");
        }


//        try{
//
//        }catch (IOException e){//compile time error, "..is never thrown"
//            e.printStackTrace();
//        }

    }
}
