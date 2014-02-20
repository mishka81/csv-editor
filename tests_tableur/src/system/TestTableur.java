package system;

import java.io.InputStream;

import gui.FenetrePrincipale;

public class TestTableur {

	public static void main(String[] args) throws Exception {
//		new FenetrePrincipale().setVisible(true);
		
		InputStream inputStream = TestTableur.class.getResourceAsStream("file.csv");
		CSVLoader.INSTANCE.loadCSV(inputStream);
	}

}
