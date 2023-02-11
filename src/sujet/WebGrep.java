package sujet;
import javax.imageio.event.IIOWriteWarningListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class WebGrep extends Thread {
	public void run() {
		Executor executor = Executors.newFixedThreadPool(Tools.numberThreads()); // create a pool of numberThread threads

		FileBLoquante urlToParse = new FileBLoquante();
		FileBLoquante historizesURL = new FileBLoquante();

		urlToParse.addAll(Tools.startingURL());

		executor.execute(() -> {
		while (!urlToParse.isEmpty()) {
				Tools.ParsedPage p = null;
				try {
					CustomNode url = urlToParse.take();
					if (!historizesURL.contains(url)) {
						// we parse the link only if it is not already in the historique
						historizesURL.put(url);
						p = Tools.parsePage(url.linkToParse);
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				// Check if matches were found
				if (!p.matches().isEmpty() && p!=null) {
					Tools.print(p);
					System.out.println("New href detected on this page : " + p.hrefs().size() + ", total hrefs left to parse : " + urlToParse.size());
					for (String s : p.hrefs()) {
						CustomNode temp = new CustomNode(s);
						if (!urlToParse.contains(temp) && !historizesURL.contains(temp)) {
							try {
								urlToParse.put(temp);
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							};
						}
					}
				}
			}
		});
	}
}






