package audio;


class ParameterChecker{

	static boolean DEBUG=false;
	enum Error{
		ARGUMENTSLENGTH,
		INCORRECTCOMMAND,
		WRONGFILEFORMAT1,
		WRONGFILEFORMAT2,
	}


	public static boolean CheckFormat(String[] args){

		ParameterChecker instance = new ParameterChecker();
		Error err =instance.checkInput(args);
		
		if(err != null) { 
			if (err == Error.WRONGFILEFORMAT1) {
				System.err.println(String.format("ERROR: %s is not a "
						+ "supported format", args[1]));
			}
			else if (err == Error.WRONGFILEFORMAT2) { 
				System.err.println(String.format("ERROR: %s is not a "
						+ "supported format", args[3]));
			}
			else if(err==Error.INCORRECTCOMMAND){
				System.err.println("ERROR: incorrect command line");
			}
			else if(err==Error.ARGUMENTSLENGTH){
				System.err.println("ERROR: incorrect command line");
			}
			else{
				System.err.println("ERROR: default error");
			}
			return false;
		} 

		return true;
	}

	// Checks to see if the inputs are used correctly
	private Error checkInput(String[] args){
		if (args.length!=4){
			return Error.ARGUMENTSLENGTH;
		}
		String par1=args[0].trim();
		String par2=args[2].trim();
		String file1 = args[1].trim(), file2 = args[3].trim();
		if(par1.equals("-f")){
			if(!file1.endsWith(".wav") && !file1.endsWith(".mp3")) {
				return Error.WRONGFILEFORMAT1;
			}
		}else if(par1.equals("-d")){
			
		}else{
			return Error.INCORRECTCOMMAND;
		}
		
		if(par2.equals("-f")){
			if(!file2.endsWith(".wav") && !file2.endsWith(".mp3")) {
				return Error.WRONGFILEFORMAT2;
			}
		}else if(par2.equals("-d")){
			
		}else{
			return Error.INCORRECTCOMMAND;
		}

		return null;
	}



}
