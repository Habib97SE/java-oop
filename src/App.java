import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import habhez0.BankLogic;

/**
 * Den här klassen innehåller min lösning för uppgift0.
 * ltu-id: habhez-0
 * Namn: Habiballah Hezarehee
 */
public class App
{


   public static void main (String[] args)
   {
      LinkedHashMap<ArrayList<String>, ArrayList<String>> map = new LinkedHashMap<ArrayList<String>, ArrayList<String>>();
      ArrayList<String> keys = new ArrayList<String>();
      ArrayList<String> values = new ArrayList<String>();

      keys.add("1");
      keys.add("2");
      keys.add("3");

      values.add("One");
      values.add("Two");
      values.add("Three");

      map.put(keys, values);

      keys.clear();
      values.clear();

      map.put(keys, values);

      keys.add("yek");
      values.add("One");

      map.put(keys, values);

      System.out.println(map);

   }
}

