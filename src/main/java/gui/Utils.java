package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Utils {
	public static ArrayList<String> getStatus() {
		String lang = Locale.getDefault().getLanguage(); // Hizkuntza kode hutsa lortzeko (adib. "es", "en", "eu")
		
		if ("es".equals(lang)) {
			return new ArrayList<String>(Arrays.asList("Nuevo", "Muy Bueno", "Aceptable", "Lo ha dado todo"));
		}
		if ("eus".equals(lang) || "eu".equals(lang)) {
			return new ArrayList<String>(Arrays.asList("Berria", "Oso Ona", "Egokia", "Oso zaharra"));
		}
		
		// Hizkuntza ezaguna ez bada, ingelesa itzultzen da (Default) 'null' itzuli ordez:
		return new ArrayList<String>(Arrays.asList("New", "Very Good", "Acceptable", "Very Used"));
	}

	public static String getStatus(int t) {
		ArrayList<String> status = getStatus();
		
		// NullPointer eta IndexOutOfBoundsException babesak:
		if (status != null && t >= 0 && t < status.size()) {
			return status.get(t);
		}
		
		return ""; // edo balio lehenetsi seguru bat
	}
}