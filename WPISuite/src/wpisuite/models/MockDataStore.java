package wpisuite.models;

import java.util.ArrayList;
import com.google.gson.*;

public class MockDataStore {

	private ArrayList<User> list;
	
	public MockDataStore()
	{
		list = new ArrayList<>();
		list.add(new User("steve",0));
		list.add(new User("fred",1));
		list.add(new User("jeff",2));
		list.add(new User("tyler",3));
	}
	
	public void addDude(String json)
	{
		Gson gson = new Gson();
		User u = gson.fromJson(json, User.class);
		list.add(u);
	}
	
	public User getDude(int idNum)
	{
		int index = 0;
		for(User u : list)
		{
			if(u.getIdNum() == idNum)
				break;
			index++;
		}
		return list.get(index);
	}
}
