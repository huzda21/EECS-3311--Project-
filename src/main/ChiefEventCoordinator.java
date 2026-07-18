package main;

import java.util.ArrayList;
import java.util.Random;

public class ChiefEventCoordinator extends Admin{
	private static ChiefEventCoordinator instance;

	private ChiefEventCoordinator(String id, String email, String password, String adminId) {
		super(id,email,password,adminId);
	}
	
	public static ChiefEventCoordinator getInstance() {
		if(instance==null) {
			instance=new ChiefEventCoordinator("cd12345678","coordinator@yorku.ca","mycoordPassword@12","ab87654321");
			
		}
		return instance;
	}
	
	public Admin generateAdminAcc(String email) {
		ArrayList<Character> chars=new ArrayList<>();
		for(char i='A';i<='Z';i++) {
			chars.add(i);
		}
		for(char i='a';i<='z';i++) {
			chars.add(i);
		}
		for(char i='0';i<='9';i++) {
			chars.add(i);
		}
		chars.add('!');
		chars.add('@');
		chars.add('#');
		chars.add('%');
		chars.add('&');
		chars.add('*');
		Random r = new Random();
		int num=100000000+r.nextInt(9000000);
		String adminId="ab"+num;
		String id="cd"+num;
		String password="";
		for(int i=0;i<8;i++) {
			int index=r.nextInt(chars.size());
			password+=chars.get(index);
		}
		Admin newAdmin= new Admin(id,email,password,adminId);
		System.out.println("Chief Corrdinator Temparary password for "+email+" is: "+password);
		return newAdmin;
		
		
		
		
	}
	@Override
	public String getRoleName() {
		// TODO Auto-generated method stub
		return "Chief Event Coordinator";
	}
}
