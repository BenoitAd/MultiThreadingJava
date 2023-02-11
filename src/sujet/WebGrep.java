package sujet;
import javax.imageio.event.IIOWriteWarningListener;
import java.io.IOException;
import java.util.List;


public class WebGrep extends Thread {

	CustomNode url;


	public WebGrep() {
		Tools.setAliveThreads(Tools.getAliveThreads() + 1);
		System.out.println("thread number : " + Tools.getAliveThreads() + " alive");
		// current url of the thread to parse, first in the list
		url = Tools.startingURL().get(0);
	}

	@Override
	public void run() {
		if (url != null) {
			// Parse the page to obtain the matching expressions and the hyperlinks
			Tools.ParsedPage p = null;
			try {
				p = Tools.parsePage(url.linkToParse);
				// remove the url from the list of urls to parse and add it to the historique list
				Tools.removeParsedUrl(url);
				System.out.println("page parsed : " + Tools.getHistoriqueURL().size());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			// Check if matches were found
			if (!p.matches().isEmpty()) {
				// Print the output
				Tools.print(p);

				// add the new hrefs found to the list of hrefs
				List<String> href = p.hrefs();
				System.out.println("href detected on this page : " + href.size());
				href.forEach((obj) -> {
					// if the href is not already in the historique list we add it
					CustomNode temp = new CustomNode(obj);
					if(!Tools.isUrlInHistorique(temp)) {
						Tools.addNotParsedUrl(temp);
					}
				});
				}
			}

		if (Tools.startingURL().size() > 0) {
			// if the number of threads is more than the number of threads allowed  we wait
			while (Tools.getAliveThreads() == Tools.numberThreads()) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			// if there are still urls to parse we create a new thread
			if (Tools.startingURL().size() > 0) new WebGrep().start();
		}

		Tools.setAliveThreads(Tools.getAliveThreads() - 1);
	}
}




