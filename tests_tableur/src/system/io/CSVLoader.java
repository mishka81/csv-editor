package system.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public final class CSVLoader {
	
	public static final String SEPARATOR = ";";
	
	private File csv;
	
	private Map<XY, SoftReference<Object>> cache = new HashMap<XY, SoftReference<Object>>();
	
	public CSVLoader(File csv) {
		super();
		this.csv = csv;
		this.cache = new HashMap<XY, SoftReference<Object>>();
	}
	
	public void loadCSV(InputStream input) throws IOException {
		// Lecture du fichier ligne par ligne
		List<String> lines = IOUtils.readLines(input);
		int y = 0;
		for (String line : lines) {
			String[] zones = StringUtils.split(line, SEPARATOR);
			int x = 0;
			for (String zone : zones) {
				// Mise en cache de chaque zone
				cache.put(new XY(x, y), new SoftReference<Object>(new String(zone)));
				y++;
			}
			x++;
		}
	}
	
	public Object getZone(XY xy) throws IOException {
		Object zone = null;
		if (cache.containsKey(xy)) {
			zone = cache.get(xy).get();
			if (zone == null) {
				// Si la référence à la zone a été supprimée par le GC, il faut la recharger depuis le fichier
				LineNumberReader lineNumberReader = null;
				try {
				lineNumberReader = new LineNumberReader(new InputStreamReader(new FileInputStream(csv)));
				String line = null;
				while ((line = lineNumberReader.readLine()) != null) {
					if (lineNumberReader.getLineNumber() == xy.getX()) {
						String[] zones = StringUtils.split(line, SEPARATOR);
						if (zones.length <= xy.getY()) {
							zone = zones[xy.getY()];
						}
						break;
					}
				}
				} finally {
					lineNumberReader.close();
				}
			}
		}
		return zone;
	}

	public File getCsv() {
		return csv;
	}

	public void setCsv(File csv) {
		this.csv = csv;
	}

}
