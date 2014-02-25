package system;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public final class CSVLoader {
	
	public static final CSVLoader INSTANCE = new CSVLoader();
	
	public static final String SEPARATOR = ";";
	
	private Map<String, SoftReference<Object>> cache = new HashMap<String, SoftReference<Object>>();
	
	private CSVLoader() {
		super();
	}
	
	public void loadCSV(InputStream input) throws IOException {
		// Lecture du fichier ligne par ligne
		List<String> lines = IOUtils.readLines(input);
		int idxLine = 0;
		int idXColumn = 0;
		for (String line : lines) {
			String[] zones = StringUtils.split(line, SEPARATOR);
			idXColumn = 0;
			for (String zone : zones) {
				// Mise en cache de chaque zone
				cache.put(idxLine + ";" + idXColumn, new SoftReference<Object>(new String(zone)));
				idXColumn++;
			}
			idxLine++;
		}
		System.out.println(cache.values().size() + " zones loaded.");
	}

}
