# Data serialization

## The data what?

Well we all know what data is right?
 
Data is just some kind of information that is usually stored in the
programs' memory during the runtime of the program.

Once we need to save the data somewhere or send it over the network to some
other application or consumer, we need to write it down in some kind of structured manner
so that the consumer of that data will understand how to read it back.

The process of writing structured data somewhere is called **serialization**.
And reading that data back in a structured manner is called **deserialization**.

## How did we serialize the data so far?

So far we have been working with `DataInputStream` and `DataOutputStream` classes
that provide useful methods (such as `readUTF` or `readInt` and similar methods for writing)
in order to serialize the data into the stream in a way that we could read it back later. 

So we did something similar in order to serialize the data:
```java
class Writer {
    public static void main(String[] args)  {
        try (final DataOutputStream outputStream = new DataOutputStream(new FileOutputStream("test.txt"))) {
            outputStream.writeInt(1);
            outputStream.writeUTF("Hello, world!");
        }
    }
}
```

And then we could read it back later:

```java
class Reader {
    public static void main(String[] args)  {
        try (final DataInputStream inputStream = new DataInputStream(new FileInputStream("test.txt"))) {
            final int number = inputStream.readInt();
            final String message = inputStream.readUTF();

            System.out.println(number);
            System.out.println(message);
        }
    }
}
```

There are several problems with this approach.

It is easy to serialize trivial messages.
What about complex classes? 
Imagine if we had a following class structure that we wanted to serialize:

```java
class Book {
    String author;
    String title;
    List<Page> pages;
}

class Page {
    int number;
    List<Line> lines;
}

class Line {
    int number;
    String text;
}
```

Now if we tried to serialize this using `DataOutputstream` and `DataInputstream` then the code would look quite complex.
And then someone says that we need to add a license to the book as well...
Which means that we have to change the whole process how we serialize and deserialize the data.

Another problem is that if we want to share the data with external applications, then they need to know
how we serialized the data.
If there were written in Java, we could tell them that in order to read the `Book` class, you first
need to read the author as a `String`, then title as a `String` and so on...

But what if the other application is not written in Java and they do not have `DataInputStream` and `DataOutputstream`classes?

Fortunately, there is a solution to those problems, which we will explore below.

## Solving the serialization format problem

So in order to solve the problem, we could use the formats that are standardized and accepted 
in the industry.

Currently, there are two most wide-spread formats that are used for data transfer: **JSON** and **XML**.


### JSON

***JSON*** - stands for JavaScript Object Notation and is a text format, which means that 
this format is easily readable by humans and also easily readable by machines.
Doesn't sound promising? Well, this is how our book class would look like if we serialized it
using JSON format:

```
{
  "author": "Underscore",
  "title": "Scala with Cats",
  "pages": [
    {
      "number": 1,
      "lines": [
        {
          "number": 1,
          "text": "Text from line 1"
        },
        {
          "number": 2,
          "text": "Text from line 2"
        }
      ]
    }
  ]
}
```

A lot of braces.
But the format is readable. 
We can clearly see what is the author of the book, what is the title, what pages it has etc.

What do you have to know about JSON? 

In JSON: 
- There are 3 primitive types that you need to remember:
    - String: which is placed between quotes `"String"` or `'String'`
    - Numbers: `1` or `1.23`
    - Booleans: `true` or `false`
- `{` denotes start of the non-primitive object and `}` denotes the end of the object
- `[` marks the start of the array and `]` marks the end of the array
- arrays can contain different types of values (both primitive and non-primitive)

More precise explanation of the format can be found on the [JSON website](https://www.json.org/json-en.html).

### XML

In addition to JSON we have **XML**.

XML stands for eXtensible Markup Language.
Most of you might be familiar with HTML, language that is used to mark up web pages.
XML is a superset of HTML, which means that it more flexible than HTML.

XML looks exactly like HTML, but you are not limited to the tag names you can use.
So let's see how our `Book` class would look in XML:

```
<Book>
    <Author>John Ajvide Lindqvist</Author>
    <Title>Let the Right One In</Title>
    <Pages>
        <Page>
            <Number>1</Number>
            <Lines>
                <Line>
                    <Number>1</Number>
                    <Text>Text on line 1</Text>
                </Line>
                <Line>
                    <Number>2</Number>
                    <Text>Text on line 2</Text>
                </Line>
            </Lines>
        </Page>
    </Pages>
</Book>
```

This text format is also readable.
Maybe less readable than JSON and a litle bit more verbose.
XML is does not have any primitive types, instead the data is treated as text, so there
is no difference between: `Text`,`true` or `1`.
However, XML does have schemas which can define types used in the XML document, but in this example
we will not go into that topic.

## But there are so many formats, which one should I use?

And the answer is... It depends...

Most modern applications use JSON format, because it is less verbose and easier to read.

XML is considered to be legacy format, because it is quite verbose and takes more physical
space than JSON.
Nonetheless, XML is used quite a lot in legacy applications, so you will probably have to work with both.

Usually modern applications can handle both XML and JSON and also produce both of those formats, depending
on which one you provide / want to get as a response.

More on the topic of content negotiation can be found [here](https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation).

## That is cool and all, but how does this help me?

How does this even help? We are using Java after all, why would we care about JSON and XML?

Well, what if I told you that whole serialization and deserialization process
can be automated, and you would't need to worry how can you serialize class `X` or `Y`?

For example, Java provides serialization and deserialization mechanism out of the box:

```java
package io.github.zukkari.examples.object;

import io.github.zukkari.data.Book;
import io.github.zukkari.generator.BookGenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ObjectStreamExample {
    public static void main(String[] args) throws Exception {
        Path tempFile = Files.createTempFile("java_object", null);
        System.out.printf("Writing to file: %s\n", tempFile.toAbsolutePath());

        Book book = BookGenerator.generate();

        // Write the object to file
        try (var stream = new ObjectOutputStream(new FileOutputStream(tempFile.toFile()))) {
            stream.writeObject(book);
        }

        try (var stream = new ObjectInputStream(new FileInputStream(tempFile.toFile()))) {
            // Read the object back from the stream
            // and cast the object to the book instance
            final Book inputBook = (Book) stream.readObject();

            System.out.println(inputBook);
        }
    }
}
```

Output: 

```
Book{author='Book author', title='Book title', pages=[Page{number=0, lines=[Line{number=0, text='fda2e223-c371-429f-af41-446291c05e96'}, Line{number=1, text='fabd264b-ae09-4e39-9cef-3586e1d1d39e'}]}, Page{number=1, lines=[Line{number=0, text='582d2fa7-4c36-4800-a0b4-0af1c25a64a1'}, Line{number=1, text='65af1e38-1233-450a-9c95-5ae14580753f'}]}], released=false}
```

Yeah this approach works and allows us to serialize object.

But there are multiple problems with this approach.

Firstly, all of the classes that are being written must implement `Serializable` interface.
This is a problem when you are using a class that you have not created yourself.

Secondly, what if you need to modify the object? 
How will you read back old version of the object into the new model?

Finally, what if you want to send this data over the network?
How will the consumer application read this object?
Lets look inside the file in order to see what Java wrote:

```
¬ķ sr io.github.zukkari.data.Book~;H­G“ŠĖ Z releasedL authort Ljava/lang/String;L pagest Ljava/util/List;L titleq ~ xp t Book authorsr java.util.ArrayListxŅ™Ēa¯ I sizexp   w   sr io.github.zukkari.data.Paget$‹¸źkO I numberL linesq ~ xp    sq ~    w   sr io.github.zukkari.data.Line€kZāH„³ I numberL textq ~ xp    t $fda2e223-c371-429f-af41-446291c05e96sq ~ 
   t $fabd264b-ae09-4e39-9cef-3586e1d1d39exsq ~    sq ~    w   sq ~ 
    t $582d2fa7-4c36-4800-a0b4-0af1c25a64a1sq ~ 
   t $65af1e38-1233-450a-9c95-5ae14580753fxxt 
Book title
```

What the hell is this? How the hell is consumer application supposed to understand this?

I hope you can see the problems here and here we will explore alternative and more standardized ways to serialize the data.

We will investigate the solutions using two libraries for Java:
[Jackson](https://github.com/FasterXML/jackson) and [Gson](https://github.com/google/gson).

Let's jump straight in and see how we can serialize and deserialize stuff using those two libraries.

## Serializing the data

First, lets look into Jackson.

We will keep using our `Book` class and work with this class.

## Jackson

Who the hell is Jackson?

Jackson is a library that is mainly used for JSON manipulations.
It has many advanced usages such as JSON streaming but we will be looking into the simple
parts of this library.

## JSON

For this example, you can open up `JacksonExample.java`.

The class contains following code:

```java
package io.github.zukkari.examples.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zukkari.data.Book;
import io.github.zukkari.generator.BookGenerator;

public class JacksonExample {
    public static void main(String[] args) throws Exception {
        // Create book that we want to serialize
        final Book book = BookGenerator.generate();

        // Create an instance of class that will serialize our book
        final ObjectMapper objectMapper = new ObjectMapper();

        // Serialize the book
        final String bookAsJson = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(book);

        // Print out the resulting book in JSON format
        System.out.println(bookAsJson);
    }
}
```

And the output of this code looks like this:

```
{"author":"Book author","title":"Book title","pages":[{"number":0,"lines":[{"number":0,"text":"5729ee26-e654-4e62-8708-d9b94ae87efb"},{"number":1,"text":"c5912b7e-4fcc-4eed-b327-6517e83c29b6"}]},{"number":1,"lines":[{"number":0,"text":"cd8942be-8786-4f8a-8844-f0e29264b06c"},{"number":1,"text":"495dc73a-00e1-4c6f-b479-0078cf0c3c50"}]}],"released":false}
```

And look! It's the book and it in JSON format already, with just few lines of code.

Let's go through this example in order to see what we actually did here and how does it work.

The following line is there just for the testing purposes, since we need to serialize something.
In order to serialize something, we need to generate a book:

```java
final Book book = BookGenerator.generate();
```

Then, the interesting part.
Here we create an instance of `ObjectMapper`.

```java
final ObjectMapper objectMapper = new ObjectMapper();
```

What is an object mapper? 
Well this is a class that Jackson provides you with so you can serialize and deserialize your
objects.

This means that we can use this mapper to convert our object to JSON:

```java
final String bookAsJson = objectMapper.writeValueAsString(book);
```

In order to write our value as a JSON, we just needed to call the `writeValueAsString` method on
the object mapper.

But you might have noticed that the JSON is not that pretty and is printed on the single line.
In order to fix that, we can use the pretty printing writer, which produces prettier output:

```java
final String bookAsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(book);
```

## XML

For XML example check `JacksonXMLExample.java`.
This example has almost no difference with the JSON example, except that we now use `XMLMapper` class
in order to serialize our book class:

````java
package io.github.zukkari.examples.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.zukkari.data.Book;
import io.github.zukkari.generator.BookGenerator;

public class JacksonXMLExample {
    public static void main(String[] args) throws Exception {
        // Create book that we want to serialize
        final Book book = BookGenerator.generate();

        // Create an instance of class that will serialize our book
        // Note that we now use XMLMapper instead of ObjectMapper
        final ObjectMapper objectMapper = new XmlMapper();

        // Serialize the book
        final String bookAsXML = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(book);

        // Print out the resulting book in XML format
        System.out.println(bookAsXML);
    }
}
````

And as a result this produces:

```xml
<Book>
  <author>Book author</author>
  <title>Book title</title>
  <pages>
    <pages>
      <number>0</number>
      <lines>
        <lines>
          <number>0</number>
          <text>1c634e84-d607-4999-90a6-def99717c9d0</text>
        </lines>
        <lines>
          <number>1</number>
          <text>1fb811cb-6ebf-4f32-9cdf-6cd92fa8b671</text>
        </lines>
      </lines>
    </pages>
    <pages>
      <number>1</number>
      <lines>
        <lines>
          <number>0</number>
          <text>851fec05-cf20-48f6-b85a-bf54e1e5956c</text>
        </lines>
        <lines>
          <number>1</number>
          <text>b2f28471-4537-47a5-ad55-d240bb5dcfef</text>
        </lines>
      </lines>
    </pages>
  </pages>
  <released>false</released>
</Book>
```

Which is exactly one expected XML would look!

So this is a very short introduction how to use the Jackson object mapper to produce JSON and XML format. 

# Gson

Okay, first we had Jackson and now Gson?

Gson is library made by Google.
Well, its not like Gmail is enough for us, we can use Google to serialize our objects as well.

## JSON

Example using Gson:

```java
package io.github.zukkari.examples.gson;

import com.google.gson.Gson;
import io.github.zukkari.data.Book;
import io.github.zukkari.generator.BookGenerator;

public class GsonExample {
    public static void main(String[] args) {
        // Create book that we want to serialize
        final Book book = BookGenerator.generate();

        // Create an instance of class that will serialize our book
        final Gson gson = new Gson();

        // Serialize the book
        final String bookAsJson = gson.toJson(book);

        // Print out the resulting book in JSON format
        System.out.println(bookAsJson);
    }
}
```

And voilà, this produces exactly the same output as Jackson (well this kind of makes sense since JSON is standardized format):
```
{"author":"Book author","title":"Book title","pages":[{"number":0,"lines":[{"number":0,"text":"23ce7819-d7a8-4489-b619-70cf0b62b738"},{"number":1,"text":"a50ae21c-1d2f-43af-881a-ae352ea26110"}]},{"number":1,"lines":[{"number":0,"text":"fbd630cf-df55-4512-a47e-778959c28cce"},{"number":1,"text":"7759153d-d9d8-4b02-8bea-9d661dfaa9e5"}]}],"released":false}
```

## XML

Unlike Jackson, Gson does not support XML serialization out of the box.
However, there is a module that does supports this, although this is not official.
The module for Gson XML can be found [here](https://github.com/stanfy/gson-xml).
It provides some examples how it can be used, but I would not recommend to use Gson for XML.

And since Gson does not support XML serialization out of the box, we can see that this is one of the differences
between Gson and Jackson.

So are there any more differences between those two?
Which one should I use?

## Differences between Jackson and Gson

As you could see both of the libraries produce similar result using similar amount
of lines of code.
For basic use cases, there is no difference which library to use since performance
differences are very small.

However, for advanced use cases, such as custom field mapping or custom serialization
structure, those libraries are configured very differently.

Further we will explore how can we create custom mappings and also see why we would need to do that.

## How does this work?

You might have a question, how does this even work? 
How does Gson or Jackson know what fields to serialize into JSON
and what names should it use for serialization?

Well to put it simply, if we dont give any directions on how to map our class,
those libraries simply look at the field types and field names inside the class
and serialize accordingly.

However, it is possibly to alternate the way classes are serialized and we will see this in the next section.

## Advanced mapping

Since the instructions for XML are similar to JSON for Jackson, and Gson does not support XML out of the box,
we will continue or examples using JSON.

Suppose we have the following scenario.
We work in an Estonian company called L33T code, and we write code in Estonian.
This means that our classes and field names are named in Estonian.
But we provide our services to customers outside of Estonia.
For example, we would like to expose our user class, which represents a user in our system.

```java
public class Kasutaja {
    private String kasutajanimi;
    private String parool;

    private int kasutajaStaatus;
    
    private boolean aktiivneKasutaja;

    public String getKasutajanimi() {
        return kasutajanimi;
    }

    public void setKasutajanimi(String kasutajanimi) {
        this.kasutajanimi = kasutajanimi;
    }

    public String getParool() {
        return parool;
    }

    public void setParool(String parool) {
        this.parool = parool;
    }

    public int getKasutajaStaatus() {
        return kasutajaStaatus;
    }

    public void setKasutajaStaatus(int kasutajaStaatus) {
        this.kasutajaStaatus = kasutajaStaatus;
    }

    public boolean isAktiivneKasutaja() {
        return aktiivneKasutaja;
    }

    public void setAktiivneKasutaja(boolean aktiivneKasutaja) {
        this.aktiivneKasutaja = aktiivneKasutaja;
    }
}
```

And now if we try to serialize this class we would get the following output:

```json
{
  "kasutajanimi": "Username1",
  "parool": "hunter2",
  "kasutajaStaatus": 3,
  "aktiivneKasutaja": true
}
```

Ouch, we just exposed our users password.
And the field names are in Estonian.
How can we change the mapping pattern so that the field names would be in English and we would not leak the password?

## Advanced mapping with Jackson

Jackson serialization process is mostly controlled using annotations.
Annotations are special Java classes, which we will talk about in later sessions.
What you currently need to know is that annotations allow you to annotate parts of your code like so:

```java
@MyCustomAnnotation
class MyClass {
}
```

Annotations by themselves don't do anything so now we will explore hoes does Jackson use those annotations.

Currently we have 2 objectives:
1. We want to hide the user password in serialized user model
1. We want to translate field names into English, so that our colleagues could understand the meaning of the fields

For the first objective, Jackson provides the following annotation `@JsonIgnore` which simply ignore the property
during the serialization process.
In order to use it, we just need to annotate the field that we want to ignore:

```java
@JsonIgnore
private String parool;
```

And voilà, user does not have password anymore when serialized:
```json
{
  "kasutajanimi": "Username1",
  "kasutajaStaatus": 3,
  "aktiivneKasutaja": true
}
```

Now to the next objective!

We need to translate the fields.
Fortunately, Jackson already provides an opportunity to rename fields in the JSON using: `@JsonProperty` annotation.

Usage is also pretty straight forward:
```java
@JsonProperty(value = "username")
private String kasutajanimi;
```

Now lets put it all together:
```java
public class Kasutaja {
    @JsonProperty(value = "username")
    private String kasutajanimi;

    @JsonIgnore
    private String parool;

    @JsonProperty(value = "userstatus")
    private int kasutajaStaatus;

    @JsonProperty(value = "isActiveUser")
    private boolean aktiivneKasutaja;

    public String getKasutajanimi() {
        return kasutajanimi;
    }

    public void setKasutajanimi(String kasutajanimi) {
        this.kasutajanimi = kasutajanimi;
    }

    public String getParool() {
        return parool;
    }

    public void setParool(String parool) {
        this.parool = parool;
    }

    public int getKasutajaStaatus() {
        return kasutajaStaatus;
    }

    public void setKasutajaStaatus(int kasutajaStaatus) {
        this.kasutajaStaatus = kasutajaStaatus;
    }

    public boolean isAktiivneKasutaja() {
        return aktiivneKasutaja;
    }

    public void setAktiivneKasutaja(boolean aktiivneKasutaja) {
        this.aktiivneKasutaja = aktiivneKasutaja;
    }
}
```

which provides output of: 
```json
{
  "username": "Username1",
  "userstatus": 3,
  "isActiveUser": true
}
```

Which is exactly what we wanted to achieve.

## Advanced mapping with Gson 

Now lets see how can we achieve similar result using Gson.

While Jackson controls the serialization through annotations, Gson takes more programmatic approach.
As you might have noticed, adding annotations means that there is only one way to serialize your object.

Gson allows you to have multiple ways to serialize a single class.
Let's implement custom serializer for our `Kasutaja` class, so that we can solve 
the same problems as we did with Jackson.

In order to create custom serialization structure for class `T`, we need to create a class that
implements `JsonSerializer<T>`.

Interface declarations looks pretty simple: 
```java
public interface JsonSerializer<T> {
    JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context);
    }
}
```

So lets implement it for our `Kasutaja` class.

Complete implementation might look similar to this:

```java
public class KasutajaSerializer implements JsonSerializer<Kasutaja> {
    @Override
    public JsonElement serialize(Kasutaja kasutaja, Type type, JsonSerializationContext jsonSerializationContext) {
        // Create new JSON object which will act
        // as a root to our tree (this represents Kasutaja object itself)
        JsonObject jsonObject = new JsonObject();
        
        // Add username property
        jsonObject.addProperty("username", kasutaja.getKasutajanimi());
        
        // Add user status
        jsonObject.addProperty("userstatus", kasutaja.getKasutajaStaatus());

        // Add active user poperty
        jsonObject.addProperty("isActiveUser", kasutaja.isAktiivneKasutaja());
        
        return jsonObject;
    }
}
```

As you can see the approach is quite different, here we get much more freedom
in what do we actually put into the JSON object.

And then to actually use this serializer, we need to register it when creating Gson object.
So now instead of creating `Gson` instance directly, we will use `GsonBuilder` instead:

```java
package io.github.zukkari.examples.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonAdvancedExample {
    public static void main(String[] args) {
        Kasutaja kasutaja = new Kasutaja();
        kasutaja.setKasutajanimi("Username1");
        kasutaja.setParool("hunter2");
        kasutaja.setKasutajaStaatus(3);
        kasutaja.setAktiivneKasutaja(true);

        Gson gson = new GsonBuilder()
                // Register our KasutajaSerializer class here
                .registerTypeAdapter(Kasutaja.class, new KasutajaSerializer())
                .create();

        String userAsJson = gson.toJson(kasutaja);

        System.out.println(userAsJson);
    }
}
```

And the output looks similar to Jackson:

```json
{
  "username": "Username1",
  "userstatus": 3,
  "isActiveUser": true
}
```

So as you can see, the tools are quite different but produce the same results.
Again it is up to you, which one do you prefer and which one do you want to use.

## Consuming serialized data through the network

So far we have seen how to produce structured data.
What about consuming the data?

Now we can finally explore the part where we consume the data.
For that we will be using [Estonian Weather service](http://www.ilmateenistus.ee/?lang=en).

They provide various types of information (forecasts, current weather state, warnings etc).
Remember what I told you about web formats?
I think I said something in the lines of "XML is legacy and is not widely used".
And you guessed it right, Estonian Weather Service uses XML to serve its data.

Since previously we focused mostly on JSON, now we will explore XML.
Remember that previously I showed that consuming JSON and XML is pretty similar, so
I am sure you will be able to figure out how to consume JSON on your own ;)

Firstly lets look at the data that they provide.
We will be looking at current weather observations which can be found [here](http://www.ilmateenistus.ee/ilma_andmed/xml/observations.php).

We can see that there is root node `observations`, which represents the list of observations.

Next, we can see that there is list of stations which are represented by `station` nodes.
Stations then have various attributes such as name (place where observation was made),
coordinates, temperature etc.

For simplicity, lets say that we are interested in the following attributes:
- name (String)
- airpressure (Double)
- relativehumidity (Double)
- airtemperature (Double)
- winddirection (Double, in degrees)
- windspeed (Double)

Now that we have analyzed the structure, lets create Java classes that will represent this structure 
in our code.

It looks that there two entities that we need to create to handle this data:
- `Observations` will hold our list of stations
- `Station` which contains attributes of station nodes that we are interested in

Translating the following to Java:

```java
package io.github.zukkari.examples.consuming.weather;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "observations")
public class Observations {

    @JacksonXmlProperty(localName = "timestamp")
    // attribute with name "timestamp" to know the time of observation
    private Integer timestamp;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "station")
    private List<Station> stations;

    // List of stations
    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Observations{" +
                "timestamp=" + timestamp +
                ", stations=" + stations +
                '}';
    }
}
```

```java
package io.github.zukkari.examples.consuming.weather;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Station {
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "airpressure")
    private Double airPressure;

    @JacksonXmlProperty(localName = "relativehumidity")
    private Double relativeHumidity;

    @JacksonXmlProperty(localName = "airtemperature")
    private Double temperature;

    @JacksonXmlProperty(localName = "winddirection")
    private Double windDirection;

    @JacksonXmlProperty(localName = "windspeed")
    private Double windSpeed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(Double airPressure) {
        this.airPressure = airPressure;
    }

    public Double getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(Double relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Double windDirection) {
        this.windDirection = windDirection;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", airPressure=" + airPressure +
                ", relativeHumidity=" + relativeHumidity +
                ", temperature=" + temperature +
                ", windDirection=" + windDirection +
                ", windSpeed=" + windSpeed +
                '}';
    }
}
```

As you can see the, mapping is pretty similar to JSON, although the annotations are a little bit different.

Previously we have been sending that over the network using sockets.
As you might have noticed, sockets are quite low level and web servers use
higher level of abstraction to communicate over the web: [HTTP](https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol).

Now that we have set up our model, we need to fetch it from the link somehow.
Here we will use `HttpClient` from Java standard library, which uses HTTP.

```java
package io.github.zukkari.examples.consuming;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.zukkari.examples.consuming.weather.Observations;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JacksonXMLWebExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Create the mapper that will transform XML into our Java object
        ObjectMapper mapper = new XmlMapper();
        // Disable feature that fails on unknown properties
        // This is needed because we dont need all of the properties
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Create HTTP client
        var client = HttpClient.newHttpClient();

        // Create a request that we will send over the network
        var request = HttpRequest.newBuilder()
                // URL that we will fetch the data from
                .uri(URI.create("http://www.ilmateenistus.ee/ilma_andmed/xml/observations.php"))
                // We have to set this header for this call, otherwise
                // Estonian Weather Service will not send us the response
                .header("User-Agent", "Mozilla/5.0")
                // HTTP method that specifies that we want to get the information
                .GET()
                .build();

        // Send the request over the network
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        // Read the Inputstream of the response into our Observations class
        Observations observations = mapper.readValue(response.body(), Observations.class);

        // Print out the result
        System.out.println(observations);
    }
}
```

There are few important bits in this code, let's go over them.

Firstly, we have to disable failure on unknown properties:

```java
mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
```

This is needed because if we don't disable this, Jackson will fail with an exception since
it does not know what to do with properties that are not present in our model.

Secondly, we need to include 'User-Agent' header in our request, otherwise
we will receive a 403 (Forbidden) response, since this web service expects you to identify
yourself before responding:

```java
header("User-Agent", "Mozilla/5.0")
```

And then we need to deserialize byte array into our `Observations` class:

```java
Observations observations = mapper.readValue(response.body(), Observations.class);
```

We use the `readValue` method from `ObjectMapper` class, which takes an `InputStream` and type of the object
that the mapper needs to deserialize this input stream into.

This pretty much sums up a simple use case on how to consume web services using Java.

## Exercise

Using [Github API](https://developer.github.com/v3/) fetch the following information about your profile:
- Your Github login name
- URL to your Github profile
- Number of public repositories
- Date when your Github profile was created

For this exercise your could either use either Gson or Jackson, whichever you prefer.
However, for practice purposes, it might be useful to try Gson, since we have already used Jackson in an example.

