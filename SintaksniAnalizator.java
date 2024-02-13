package drugi;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

 public class SintaksniAnalizator {
	    public static void main(String[] args) {
	    	
	    	ArrayList<String> ispisGenStablo = new ArrayList<String>();
	    	//        TreeNode root = new TreeNode("<program>");

	          ArrayList<Integer> brojPrazninaZaIspisniZn = new ArrayList<Integer>();
    	
    	Stack<String> stack = new Stack<String>(); // inicijalizacija stacka
        
        ArrayList<String> LStraneGramatike = new ArrayList<String>(); // nezavrsni znakovi na L strani gramatike
        ArrayList<String> DStraneGramatike = new ArrayList<String>(); //znakovi na D strani gramatike
        
        ArrayList<String> skupPrimijeni = new ArrayList<String>(); // primijeni skupovi za produkcije gramatike
        
        //liste se pune vrijednostima zadanim u tekstu zadatka
         gramatikaPJ(LStraneGramatike, DStraneGramatike);
         primijeniZaGramatiku(skupPrimijeni);
        //System.out.println(LStraneGramatike);
        //System.out.println(DStraneGramatike);
        //System.out.println(skupPrimijeni);

         
        
          LinkedList<Integer> brojPrazninaZaZnstacka = new LinkedList<Integer>();
        
           Scanner sc1 = new Scanner(System.in);
           String redak = sc1.nextLine();
           stack.add("#"); // # == znak za dno stacka
           stack.add("<program>"); // uvijek se stavlja <program> kao pocetni nezavrsni znak
           
           brojPrazninaZaZnstacka.add(0); // prije <program> u ispisu nema praznina

          while ((stack.size() > 1) || (sc1.hasNextLine() == true)) {

            if (redak.equals("!") && (stack.size() == 1)) { // ! == znak za kraj ulaza
                 break; //prihvacen niz
            }

            String uniZnak = redak.split(" ")[0]; // iz retka na ulazu se spremi u uniZnak prva vrijednost prije " "
            //String rBrRetka = redak.split(" ")[1];
            //String leksJed = redak.split(" ")[2];
            
            Integer pronadeno = 0; // je li za trenutni uniZnak i znak na vrhu stacka postoji primijeni skup 
            
            if (stack.peek().equals(uniZnak) == true){
                stack.pop();
                ispisGenStablo.add(redak);
                
                Integer br = brojPrazninaZaZnstacka.removeLast(); // za "list identifikator" uzme se njegov broj praznina 
                brojPrazninaZaIspisniZn.add(br);

                if (sc1.hasNextLine() == true){
                    redak = sc1.nextLine();
                    if (redak.trim().isEmpty()) {
                        redak = "!"; //ako nema vise elemenata na ulazu na slj. retku -> slj redak dobije vrijednost kraja niza !
                    }
                } 
                else {
                    redak = "!";
                }

                 continue;
            }
            
            else if ("$".equals(stack.peek()) == true){ // na vrhu stacka == $
            	String el = stack.pop();
                ispisGenStablo.add(el);
                
                Integer count = brojPrazninaZaZnstacka.removeLast();
                brojPrazninaZaIspisniZn.add(count);
                 continue;
            } 
            
      
            int i;
             for (i = 0; i < LStraneGramatike.size(); ++i) {
               if ((skupPrimijeni.get(i).contains(uniZnak)) && (Objects.equals(LStraneGramatike.get(i), stack.peek()))) {
                	
                    int trenutniBrPraznZaDubinu = brojPrazninaZaZnstacka.removeLast(); 
                    brojPrazninaZaIspisniZn.add(trenutniBrPraznZaDubinu);
                    
                    // System.out.println(trenutniBrPraznZaDubinu);
                     pronadeno = 1;

                    List<String> produkcijeSplitList =List.of(DStraneGramatike.get(i).split(" "));
                    
                    Integer brojProdukcija = produkcijeSplitList.size();
                    for (int j = 1; j <= brojProdukcija; ++j) {
                        brojPrazninaZaZnstacka.add(1+trenutniBrPraznZaDubinu);
                    }
                    
                     String vrhSt = stack.pop();
                     ispisGenStablo.add(vrhSt);
                    
                    List<String> reversedDStraneGramatike = new ArrayList<String>(produkcijeSplitList);
                    Collections.reverse(reversedDStraneGramatike);

                    // stavi produkcije na stack
                    for (String produkcija : reversedDStraneGramatike) {
                        stack.push(produkcija);
                    }
                    break;
                }
            }

              if (pronadeno == 0) {
            	 //oslobada se memorija i pripremaju liste za nove elemente -> brise se njihov sadrzaj
            	 brojPrazninaZaIspisniZn = new ArrayList<Integer>();
            	 ispisGenStablo = new ArrayList<String>();    
            
            	 ispisGreska(redak,stack);
                 break;
              }
          }
        
        sc1.close();
        ispisiGeneriranoStablo(ispisGenStablo, brojPrazninaZaIspisniZn);
    }

    private static void ispisiGeneriranoStablo(List<String> ispisGenStablo, List<Integer> brojPrazninaZaIspisniZn) {
        int a, b;
        int brZnUSt = ispisGenStablo.size();
    	for (a = 0; a < brZnUSt; ++a) {
            for (b = 0; b < brojPrazninaZaIspisniZn.get(a); ++b) {
                System.out.printf(" "); // ispise se odredeni broj praznina prije svakog zn. u stablu
            }
            System.out.println(ispisGenStablo.get(a)); // ispise se znak u stablu
        }
    }
    
    private static void ispisGreska(String redak, Stack<String> stack) {

        if ((stack.peek().equals("KR_AZ"))) {
            System.out.println("err" + " " + "kraj"); // ako se umjesto operatora pridruzivanja izbaci kljucna rijec az koja zavrsava za petlju
        }
        
        else if ((redak.equals("!"))) {
            System.out.println("err" + " " + "kraj"); // greska detektirana kad je ulaz prazan
        }
        else {
        	System.out.println("err" + " " + redak); //
        }
    }

    
    private static void gramatikaPJ(List<String> LStraneGramatike, List<String> DStraneGramatike) {
    		// inicijalizacije 
			LStraneGramatike.add("<program>");
			DStraneGramatike.add("<lista_naredbi>");
			
			LStraneGramatike.add("<lista_naredbi>");
			LStraneGramatike.add("<lista_naredbi>");
			DStraneGramatike.add("<naredba> <lista_naredbi>");
			DStraneGramatike.add("$");
			
			LStraneGramatike.add("<naredba>");
			LStraneGramatike.add("<naredba>");
			DStraneGramatike.add("<naredba_pridruzivanja>");
			DStraneGramatike.add("<za_petlja>");
			
			LStraneGramatike.add("<naredba_pridruzivanja>");
			DStraneGramatike.add("IDN OP_PRIDRUZI <E>");
			
			LStraneGramatike.add("<za_petlja>");
			DStraneGramatike.add("KR_ZA IDN KR_OD <E> KR_DO <E> <lista_naredbi> KR_AZ");
			
			LStraneGramatike.add("<E>");
			DStraneGramatike.add("<T> <E_lista>");
			
			LStraneGramatike.add("<E_lista>");
			LStraneGramatike.add("<E_lista>");
			LStraneGramatike.add("<E_lista>");
			DStraneGramatike.add("OP_PLUS <E>");
			DStraneGramatike.add("OP_MINUS <E>");
			DStraneGramatike.add("$");
			
			LStraneGramatike.add("<T>");
			DStraneGramatike.add("<P> <T_lista>");
			
			LStraneGramatike.add("<T_lista>");
			LStraneGramatike.add("<T_lista>");
			LStraneGramatike.add("<T_lista>");
			DStraneGramatike.add("OP_PUTA <T>");
			DStraneGramatike.add("OP_DIJELI <T>");
			DStraneGramatike.add("$");
			
			LStraneGramatike.add("<P>");
			LStraneGramatike.add("<P>");
			LStraneGramatike.add("<P>");
			LStraneGramatike.add("<P>");
			LStraneGramatike.add("<P>");
			DStraneGramatike.add("OP_PLUS <P>");
			DStraneGramatike.add("OP_MINUS <P>");
			DStraneGramatike.add("L_ZAGRADA <E> D_ZAGRADA");
			DStraneGramatike.add("IDN");
			DStraneGramatike.add("BROJ");
    }
    
    
    private static void primijeniZaGramatiku(List<String> skupPrimijeni) {
    	// za <program>
		skupPrimijeni.add("IDN KR_ZA !");
		
		// za <lista_naredbi>
		skupPrimijeni.add("IDN KR_ZA");
		skupPrimijeni.add("KR_AZ !");
		
		// za <naredba>
		skupPrimijeni.add("IDN");
		skupPrimijeni.add("KR_ZA");
		
		// za <naredba_pridruzivanja>
		skupPrimijeni.add("IDN");
		
		// za <za_petlja>
		skupPrimijeni.add("KR_ZA");
		
		// za <E>
		skupPrimijeni.add("IDN BROJ OP_PLUS OP_MINUS L_ZAGRADA");
		
		// za <E_lista>
		skupPrimijeni.add("OP_PLUS");
		skupPrimijeni.add("OP_MINUS");
		skupPrimijeni.add("IDN KR_ZA KR_DO KR_AZ D_ZAGRADA !");
		
		// za <T>
		skupPrimijeni.add("IDN BROJ OP_PLUS OP_MINUS L_ZAGRADA");

		// za <T_lista>
		skupPrimijeni.add("OP_PUTA");
		skupPrimijeni.add("OP_DIJELI");
		skupPrimijeni.add("IDN KR_ZA KR_DO KR_AZ OP_PLUS OP_MINUS D_ZAGRADA !");
		
		// za <P>
		skupPrimijeni.add("OP_PLUS");
		skupPrimijeni.add("OP_MINUS");
		skupPrimijeni.add("L_ZAGRADA");
		skupPrimijeni.add("IDN");
		skupPrimijeni.add("BROJ");
    }
    
    
}
 