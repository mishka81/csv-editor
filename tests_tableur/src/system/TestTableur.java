package system;

import java.io.InputStream;

import gui.FenetrePrincipale;

public class TestTableur {

	public static void main(String[] args) throws Exception {
<<<<<<< HEAD
		new FenetrePrincipale().setVisible(true);
		
//		InputStream inputStream = TestTableur.class.getResourceAsStream("file.csv");
//		CSVLoader.INSTANCE.loadCSV(inputStream);
=======
//		new FenetrePrincipale().setVisible(true);
		
		InputStream inputStream = TestTableur.class.getResourceAsStream("file.csv");
		CSVLoader.INSTANCE.loadCSV(inputStream);
>>>>>>> 0e666491a087619e0d4d6af9d839d44fedc6b0c4
	}

}
