/** 
 * Professor: Abbas Moghtanei
 * Class: CS211S, Advanced Java: Standard Edition 
 * @author Victor Malchikov
 * Homework #4
 * File: jr.java 
 * Assignment: Create a class that will take command line arguments (name of Class)
 *             and various options. Based on Class name and options return info
 *             to the user. 
 */

import java.lang.reflect.*;

public class jr 
{
   //-v variable/constants 
   //-m methods
   //-c constructor
   //-p private
   //-P protected (capP)
   //-u public
   //-i interface
   //-S super-class (capS)
   //-a display all
   private static boolean v, m, c, p, capP, u, i, capS, a = false;
   //object is name of the class
	private static String object;
	//cl will contain the actual class
	private static Class<?> cl;
	
	public static void main(String[] args) throws SecurityException,
	ClassNotFoundException, IllegalArgumentException, IllegalAccessException,
	InstantiationException
	{
		//first get user's options and class
		getUserInput(args);
		
		//find if user entered valid object
		checkObject();
		
		//print info to user based on options
		printClassInfo();
		
		//print constructors 
      if(a || c)
         getConstructors();		
		
      //print variables and constants 
 		if(a || v)
			getVariablesAndConstants();
		
 		//print methods 
		if(a || m)
		   getMethods();

		//closing bracket
		System.out.println("}");
		
	}
	
	//********************************printClassInfo()*******************************
	private static void printClassInfo()
	{
	  //parse class name 
	  int mod = cl.getModifiers();
	  //modifiers
	  String modifiers = Modifier.toString(mod);
	  //parse class name
	  String name = getClassName();
	  //check for gen para
	  String gen = checkClassGenPara();
	  //interface
	  String interfaceAdd = "";
	  String extendsAdd = "";
	  
	  //check for superClass (if-asked)
	  if(a || capS)
        extendsAdd = getSuperClass();
	  
	  //check for interfaces (if-asked)
	  if(a || i)
	     interfaceAdd = getInterfaces();
	  
	  //put together class information 
	  String classLine = modifiers + " " + name +gen+ " " +
	                     extendsAdd + " " + interfaceAdd + " {";
	  //display class info 
	  System.out.println(classLine);
	}
	
	//********************************getClassName()*********************************
	private static String getClassName()
	{
	   String name ="";
	   //this helps determine if class or interface 
	   if(cl.toString().contains("class"))
	      name += "class";
	   
	   return name+" "+cl.getSimpleName();
	}
	
	//********************************checkClassGenPara()****************************
	private static String checkClassGenPara()
	{
	   //check for generic type of class
	   String info ="";
	   //obtain generic parameters 
	   Type[] t = cl.getTypeParameters();
	   for(int i = 0; i < t.length; i++)
	   {
	      //parse parameters and combine together 
	      String tmp = t[i].toString();
	      if(i == 0)
	         info = "<"+tmp;
	      else
	         info += ", "+tmp;
	      
	   }
	   
	   return info.contains("<") ? info+">" : "";
	}
	
	//********************************getSuperClass()********************************
	private static String getSuperClass()
	{
	   //obtain super-class 
	   Class<?> sC = cl.getSuperclass();
	   Type t = cl.getGenericSuperclass();
	   //check if no super-class (ie. if Interface) OR if super-class is Object 
	   //do not return info if super-class is Object
	   if(sC == null || sC.getSimpleName().equals("Object"))
	      return "";
	   else
	   {
	      //prase super-class name
	      String tmp = t.toString();
	      tmp = tmp.substring(tmp.lastIndexOf(".")+1, tmp.length());
	      return "extends " +tmp;
	   }
	   
	}
	
	//********************************getInterfaces()********************************
	private static String getInterfaces()
	{
	   //parse interface and obtain all of them in the class
	   Class<?> c[] = cl.getInterfaces(); 
	   Type[] t = cl.getGenericInterfaces();
	   String info = "";
	   for(int i = 0; i < c.length; i++)
	   {
	      
	      String tmp = t[i].toString();
	      //parse generic interface
	      if(tmp.contains("<") && tmp.contains(">"))
	      {
	         String genName = c[i].getSimpleName();
	         String gen = tmp.substring(tmp.lastIndexOf("<")+1, tmp.length());
	         if(gen.contains("."))
	            gen = gen.substring(gen.lastIndexOf(".")+1, gen.length());
	         tmp = genName+"<"+gen;
	      }
	      else
	         tmp = tmp.substring(tmp.lastIndexOf(".")+1, tmp.length());
	      
	      //properly combine interfaces 
	      if(i == 0)
	         info = "implements "+tmp;
	      else
	         info += ", "+tmp;

	   }
  
	 return info;
	   
	}
	
	//********************************getConstructors()******************************
	private static void getConstructors() throws SecurityException, 
	                                             ClassNotFoundException
	{
	   //print message to the user
	   printInfo("c");
	   
	   //get all constructors
	   Constructor<?> c[] = Class.forName(object).getDeclaredConstructors();
	   
	   //get public 
	   if(a || u)
	      printPublicConstructors(c);
	   //get private
	   if(a || p)
	      printPrivateConstructors(c);
	   //get protected 
	   if(a || capP)
	      printProtectedConstructors(c);
	   
	}
	
	//********************************printPublicConstructors()**********************
	private static void printPublicConstructors(Constructor<?> c[])
	{
	   for(Constructor<?> d : c)
	   {
	      if(Modifier.isPublic(d.getModifiers())) //check modifier
	      {
	         //get modifiers 
	         String modifiers = getConstructorModifiers(d);
	         //print modifiers
	         printConstructors(d, modifiers);
	      }
	      else
	      {
	         if(!Modifier.isPrivate(d.getModifiers()) && 
	            !Modifier.isProtected(d.getModifiers()))
	            printConstructors(d, "public"); //add public modifier if no modifier          
	      }
	         
	   }
	}
	
	//********************************printPrivateConstructors()*********************
	private static void printPrivateConstructors(Constructor<?> c[])
	{
      for(Constructor<?> d : c)
      {
         if(Modifier.isPrivate(d.getModifiers())) //check modifier
         {
            //get modifiers 
            String modifiers = getConstructorModifiers(d);
            //print modifiers
            printConstructors(d, modifiers);
         }
      }	   
	}
	
	//********************************printProtectedConstructors()*******************
	private static void printProtectedConstructors(Constructor<?> c[])
	{
      for(Constructor<?> d : c)
      {
         if(Modifier.isProtected(d.getModifiers())) //check modifier
         {
            //get modifiers 
            String modifiers = getConstructorModifiers(d);
            //print modifiers
            printConstructors(d, modifiers);
         }
      }  	   
	}
	
	//********************************printConstructors()****************************
	private static void printConstructors(Constructor<?> d, String mod)
	{
	   //use mod - for modifier
	   //get name of constructor
	   String conName = d.getName();
	   //construct parsed constructor name
	   conName = conName.substring(conName.lastIndexOf(".")+1, conName.length());
	   //get parameters
	   String para = getConstructorParameters(d);
	   //get exceptions
	   String exceptions = getConstructorExceptions(d);
	   
	   System.out.println(mod + " " + conName+"("+para+") "+ exceptions);
	}
	
	//********************************getConstructorExceptions()*********************
	private static String getConstructorExceptions(Constructor<?> d)
	{
	   String exception = "";
	   //obtain exceptions
	   Class<?>[] e = d.getExceptionTypes();
      for(int i = 0; i < e.length; i ++)
      {
         //combine exceptions properly for visual display 
         if(i == 0)
            exception = "throws "+e[i].getSimpleName();
         else
            exception += ", "+e[i].getSimpleName();
      }
      return exception;	   
	}
	
	//********************************getConstructorParameters()*********************
	private static String getConstructorParameters(Constructor<?> d)
	{
	   //get parameters inside constructor
	   Class<?> c[] = d.getParameterTypes();
	   Type t[] = d.getGenericParameterTypes();
	   String para = "";
	   for(int i = 0; i < c.length; i++)
	   {
	      //properly parse and combine constructor's parameters 
	      if(t[i].toString().contains("<") && t[i].toString().contains(">"))
	      {
	         //check if generic or regular parameter type
	         String name = c[i].getSimpleName();
	         String tmp = t[i].toString();
	         tmp = tmp.substring(tmp.indexOf("<")+1, tmp.indexOf(">"));
	         String a[] = tmp.split(",");
	         //construct proper generic type
	         for(int j= 0; j < a.length; j++)
	         {
	            if(j == 0)
	               tmp = a[j].substring(a[j].lastIndexOf(".")+1, a[j].length());
	            else
	               tmp += ", "+a[j].substring(a[j].lastIndexOf(".")+1,
	                                                             a[j].length());
	         }
	         
	         if(i ==0)
	            para = name+"<"+tmp+">";
	         else
	            para += ", "+name+"<"+tmp+">";
	      }
	      else
	      {
	         if(i == 0)
	            para = c[i].getSimpleName();
	         else
	            para += ", "+c[i].getSimpleName();
	      }
	      
	   }
	   return para;
	}
	
	//********************************getConstructorModifiers()**********************
	private static String getConstructorModifiers(Constructor<?> d)
	{
	   int mod = d.getModifiers(); //obtain constructor's modifier 
	   return Modifier.toString(mod); //return string form of modifier
	}
	
	//**************METHODS**********************************************************
	
	//********************************getMethods()***********************************
	private static void getMethods() throws SecurityException, ClassNotFoundException 
	{
	   //print message to the user
	   printInfo("m");
	   
	   //get all methods
	   Method m[] = Class.forName(object).getDeclaredMethods();
	   
	   //show public information 
	   if(a || u)
	      printPublicMethods(m);
	   //show private information
	   if(a || p)
	      printPrivateMethods(m);
	   //show protected information 
	   if(a || capP)
	      printProtectedMethods(m);

	}
	
	//********************************printProtectedMethods()************************
	private static void printProtectedMethods(Method[] m)
	{
      for(Method d : m)
      {
         if(Modifier.isProtected(d.getModifiers())) //check if protected 
         {
            //get modifiers
            String modifiers = getMethodModifiers(d);
            printMethod(d, modifiers);
         }
      }   	   
	}
	
	//********************************printPrivateMethods()**************************
	private static void printPrivateMethods(Method[] m)
	{
      for(Method d : m)
      {
         if(Modifier.isPrivate(d.getModifiers())) //check if private 
         {
            //get modifiers
            String modifiers = getMethodModifiers(d);
            printMethod(d, modifiers);
         }
      }	   
	}
	
	//********************************printPublicMethods()***************************
	private static void printPublicMethods(Method[] m)
	{
	   for(Method d : m)
	   {
	      if(Modifier.isPublic(d.getModifiers())) //check if public
	      {
	         //get modifiers
	         String modifiers = getMethodModifiers(d);
	         printMethod(d, modifiers);
	      }
	      else
	      {
	         if(!Modifier.isPrivate(d.getModifiers()) && 
	            !Modifier.isProtected(d.getModifiers()))
	            printMethod(d, "public"); //attach public if it did not contain 
	      }
	   }
	}
	
	//********************************printMethod()**********************************
	private static void printMethod(Method d, String mod)
	{
      //modifier
	   String modifiers = mod;
	   //get return type
      String returnType = getReturnType(d);
      //get signature
      String name = d.getName()+"(";
      //get parameter types
      String para = getMethodParameters(d)+")";
      //get exceptions
      String exception = getMethodException(d);
      System.out.print(modifiers + " " + returnType + " " + 
                       name + para+ " " + exception+"\n");

	}
	
	//********************************getMethodException()***************************
	private static String getMethodException(Method d)
	{
	   //obtain method exceptions 
	   Class<?> e[] = d.getExceptionTypes();
	   String exception = "";
	   //parse method exceptions for display
	   for(int i = 0; i < e.length; i ++)
	   {
	      //combine method exceptions in proper order 
	      if(i == 0)
	         exception = "throws "+e[i].getSimpleName();
	      else
	         exception += ", "+e[i].getSimpleName();
	   }
	   return exception;
	}
	
	//********************************getMethodParameters()**************************
	private static String getMethodParameters(Method d)
	{
	   //obtain method parameters 
	   Class<?>[] reg = d.getParameterTypes();
	   Type[] gen = d.getGenericParameterTypes();
	   
	   String finalized ="";
	   //properly parse parameters for display 
	   for(int i = 0; i < reg.length; i++)
	   {
	   
	      String tmp = gen[i].toString();
	      //check if parameters are generic type 
         if(tmp.contains("<"))
         {
            String tmpSimple = reg[i].getSimpleName();
            tmp = tmp.substring(tmp.indexOf("<"), tmp.length());
            String[] a = tmp.split(",");
            for(int j = 0; j < a.length; j++)
            {
               if(j == 0)
                  tmp = a[j].substring(a[j].lastIndexOf(".")+1, a[j].length());
               else
                  tmp += ", "+a[j].substring(a[j].lastIndexOf(".")+1, a[j].length());
            }
            finalized = tmpSimple+"<"+tmp;
         }
         else
            finalized = reg[i].getSimpleName();

	   }
	 
	   return finalized;

	}
	
	//********************************getReturnType()********************************
	private static String getReturnType(Method d)
	{
	   //obtain return type 
	   Class<?> reg = d.getReturnType();
	   Type gen = d.getGenericReturnType();
	   String tmp = gen.toString();
	   
	   //parse return type for display
	   //check if generic
	   if(tmp.contains("<"))
      {
	      //parse generic type
         String tmpSimple = reg.getSimpleName();
         if (tmp.indexOf(tmpSimple) == -1)
            return tmpSimple;
         tmp = tmp.substring(tmp.indexOf(tmpSimple), tmp.length());
         tmp = tmp.substring(tmp.indexOf("<")+1, tmp.indexOf(">"));
         String[] a = tmp.split(",");
         
         //construct generic type for display
         for(int i = 0; i < a.length; i++)
         {
            if(i==0)
               tmp = a[i].substring(a[i].lastIndexOf(".")+1, a[i].length());
            else
               tmp += ", "+a[i].substring(a[i].lastIndexOf(".")+1, a[i].length());
         }
         tmp = tmpSimple +"<"+tmp+">";

      }
	   else
	      tmp = reg.getSimpleName();
	  
 	   return tmp;
	}
	
	//********************************getMethodModifiers()***************************
	private static String getMethodModifiers(Method d)
	{
	     int mod = d.getModifiers(); //obtain modifiers 
	     return Modifier.toString(mod); //return string version 
	}
	
	//*******VARIABLES**********************************************************////
	
	//********************************getVariablesAndConstants()*********************
	private static void getVariablesAndConstants() throws SecurityException, 
	                                  ClassNotFoundException, InstantiationException 
	{
		//print message to the user 
		printInfo("v");
		
		//get all the fields
		Field f[] = Class.forName(object).getDeclaredFields();
		
		//show public information 
		if(a || u)
		   printPublicFields(f);
		//show private information
		if(a || p)
		   printPrivateFields(f);
		//show protected information
		if(a || capP)
		   printProtectedFields(f);
		
	}
	
	//********************************printProtectedFields()*************************
	private static void printProtectedFields(Field[] f) throws InstantiationException
	{
	   for(Field d : f)
      {
         if(Modifier.isProtected(d.getModifiers())) //check if protected
         {
            try
            {
               //get modifiers
               String modifiers = getModifiers(d);
               printFieldsAndConstants(d, modifiers);
            }
            catch(IllegalAccessException e) {}
         }
      }
	}
	
	//********************************printPrivateFields()****************************
	private static void printPrivateFields(Field[] f) throws InstantiationException
	{
	   for(Field d : f)
      {
         try
         {
            if(Modifier.isPrivate(d.getModifiers())) //check if private
            {  
               //get modifiers
               String modifiers = getModifiers(d);
               printFieldsAndConstants(d, modifiers);
            }
         }
         catch(IllegalAccessException e) { }
      }
	}
	
	//********************************printPublicFields()****************************
	private static void printPublicFields(Field[] f) throws InstantiationException
	{
	   for(Field d : f)
	   {
	      try
         {
	         if(Modifier.isPublic(d.getModifiers())) //check if public 
	         {
	            //get modifiers
	            String modifiers = getModifiers(d);
	            printFieldsAndConstants(d, modifiers);
	         }
	         else
	            printFieldsAndConstants(d, "public"); //add public modifier if needed
         }
	         catch(IllegalAccessException e) { }
		}

	}
	
	//********************************printFieldsAndConstants()**********************
	private static void printFieldsAndConstants(Field d, String mod) throws 
	                                   InstantiationException, IllegalAccessException
	{
      String modifiers = mod;
	   //get type
      String type = getType(d);
      //get name
      String name = getFieldName(d);
      //get field value
      String value = getFieldValue(d, type);
      String trimString = (modifiers + " " + type + " " + name + " = " + value).trim();
      System.out.print(trimString+";\n");
	}
	
	
	//********************************getFieldName()*********************************
	private static String getFieldName(Field d) throws 
	                                   InstantiationException, IllegalAccessException
	{
	   String tmp = d.toString(); //get un-edited name
	   tmp = tmp.substring(tmp.lastIndexOf(".")+1, tmp.length()); //parse name
	   return tmp;
	}
	
	private static String getFieldValue(Field d, String type)                            
	{
	   try
      {  
	      
	      //get class Object 
         cl = Class.forName(object, true, jr.class.getClassLoader()); 
         d.setAccessible(true);
         //get object
         Object obj = cl.newInstance();
         
         //get value
         Object value = d.get(obj);
         
         //return value based on type
         switch (type)
         {
            case "Integer" :return value != null? value.toString() : "null";
            case "int" : return value != null? value.toString() : "null";
            case "Short" : return value != null? value.toString() : "null";
            case "short" : return value != null? value.toString() : "null";
            case "Long" : return value != null? value.toString() : "null";
            case "long" : return value != null? value.toString() : "null";
            case "Double" : return value != null? value.toString() : "null";
            case "double" : return value != null? value.toString() : "null";
            case "Boolean" : return value != null? value.toString() : "null";
            case "boolean" : return value != null? value.toString() : "null";
            case "Character" : return value != null? value.toString() : "null";
            case "char" : return value != null? value.toString() : "null";
            case "String" : return value != null? value.toString() : "null";
            case "Byte" : return value != null? value.toString() : "null";
            case "byte" : return value != null? value.toString() : "null";
            case "Float" : return value != null? value.toString() : "null";
            case "float" : return value != null? value.toString() : "null";
            default : return "null";
         }
      } 
      catch (ClassNotFoundException | IllegalAccessException e)
      {
         
         return "null";
      }
	   catch(InstantiationException e)
	   {

	      return ";";
	   }

	}

	//********************************getType()**************************************
 	private static String getType(Field d)
	{
 	   //get field Type 
 	   Type t = d.getGenericType();
      Class<?> c = d.getType();
      String type = t.toString();
      //check if generic
      if(type.contains("<") && type.contains(">"))
      {
         //properly parse for display
         String tmp = type.substring(type.indexOf("<")+1, type.indexOf(">"));
         String[] a = tmp.split(",");
         //combine generic types together 
         for(int j = 0; j < a.length; j++)
         {
            if(j == 0)
               tmp = a[j].substring(a[j].lastIndexOf(".")+1, a[j].length());
            else 
               tmp += ", "+(a[j].substring(a[j].lastIndexOf(".")+1, a[j].length()));
         
         }
         type = c.getSimpleName();
         //return parsed 
         tmp = type+"<"+tmp+">";
         return tmp;

      } //check if array 
      else if(d.toString().contains("[") && d.toString().contains("]")) 
      {
         String tmp = d.toString(); 
         //parse array
         tmp = tmp.substring(0, tmp.indexOf("]")+1);
         tmp = tmp.substring(tmp.lastIndexOf(".")+1, tmp.length());
         return tmp;
      }
      else
      {  //return name
         type = type.substring(type.lastIndexOf(".")+1, type.length()); 
         return type;
      }
         
	}
 	
   //********************************getModifiers()*********************************
	private static String getModifiers(Field d)
	{
	   int mod = d.getModifiers(); //get modifier
	   return Modifier.toString(mod); //return string form of modifier 
	}
	
	//********USER INPUT + FORMATTING***********
	
	//********************************printInfo()************************************
	private static void printInfo(String option)
	{
		String msg = ""; 
		//construct msg based on option
		switch (option)
		{
		   case "v" : msg = "Variables and Constants:";
		   	break;
		   case "m" : msg = "Methods:";
		   	break;
		   case "c" : msg = "Constructors:";
		   	break;
		   case "i" : msg = "Interfaces:";
		   	break;
		   case "S" : msg = "Superclass:";
		    break;
		}
		//print msg
		System.out.println("//*************** " + msg + " ***************");
		
	}
	
	//********************************checkObject()**********************************
	private static void checkObject()
	{
		//ensure that user passed in appropriate info on cmd line
	   try
		{
			cl = Class.forName(object);
		} 
		catch (ClassNotFoundException e)
		{
			//prompt user if unable to provide info 
		   System.err.println("Please provide full package name for " + object);
			System.exit(3);
		}

	}
	
	//********************************getUserInput()*********************************
	private static void getUserInput(String[] array)
	{
		String option; 
		//parse command line arguments to obtain correct info for the user 
		for(int j = 0; j < array.length; j++)
		{
			option = array[j];
			switch (option)
			{
				case "-v" : v = true; //variables and constants 
					break;
				case "-m" : m = true; //methods
					break;
				case "-c" : c = true; //constructors
					break;
				case "-p" : p = true; //private
					break;
				case "-P" : capP = true; //protected
					break;
				case "-u" : u = true; //public
					break;
				case "-i" : i = true; //interfaces 
					break;
				case "-S" : capS = true; //super-class
					break;
				case "-a" : a = true; //everything 
					break;
				default : object = array[j]; //info on class
					break;

			}//end switch
		}//end for-loop
		
		//prompt user for class name if none was passed
		if(object == null) //if no class- exit
		{
			System.err.println("Need a Class");
			System.exit(1);
		}
		//prompt user for more info if lack of options passed + no class name passed
		if(!v && !m && !c && !p && !capP && !u && !i && !capS && !a && 
				(object == null)) //if not enough info from user -exit
		{
			System.err.println("Please check input.");
			System.exit(2);
		}
		//if no options passed prompt user
      if(!v && !m && !c && !p && !capP && !u && !i && !capS && !a && 
            object != null) //if not enough info from user -exit
      {
         System.err.println("Please check input.");
         System.exit(3);
      }		
      
      if(!u && !p && !capP)
         u = p = capP = true;
     
      
	}
}//end program 
