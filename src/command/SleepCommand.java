package command;

import java.io.IOException;

public class SleepCommand implements Command {

	@Override
	public int doCommand(String[] args) throws IOException {
		try {
			Thread.sleep(Long.parseLong(args[1]));
		} catch (NumberFormatException | InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
