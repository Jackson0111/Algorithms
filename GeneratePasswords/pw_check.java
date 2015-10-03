import java.util.*;
import java.io.*;
import java.lang.StringBuilder;

public class pw_check{
	
	public boolean isEmpty = true; // THIS INDICATES IF THE TRIE IS EMPTY
	public Node rootNode; // THIS IS THE VERY FIRST NODE IN THE TRIE, THE HEAD OF THE TOPMOST LINKEDLIST
	
	public static void main(String[] args) throws IOException{
		pw_check pw = new pw_check();
		pw.generateDicTrie();
		pw.generateGoodPasswords();
		
		
	}
	
	
	// generate the list of good passwords and write to good_passwords.txt
	public void generateGoodPasswords(){
		/*
		 * passwords to be 5 characters, 1-3 of which must be letters (lowercase "a"-"z", no capitals)
		 * 1-2 of which must be numbers
		 * and 1-2 of which must be symbols (specifically "!", "@", "$", "%", "&", or "*"). 
		 * A password is considered to be good if it does not CONTAIN any of the 500 most used English words (words from dictionary.txt)
		 * or any of these words with one or more numbers substituted for letters (i.e., "7" for "t", "4" for "a", "0" for "o", "3" for "e", "1" for "i", "1" for "l", or "5" for "s").
		 * 
		 * 
		 * 
		 */
	    char[] letters = {'b','c','d','e','f','g','h','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	    char[] numbers = {'0','1','2','3','5','6','7','8','9'};
		char[] symbols = {'!','@','$','%','&','*'};
	    HashSet<StringBuilder> comb = new HashSet<StringBuilder>();
	    
		//case 1-------- 1 letter + 2 numbers + 2 symbols
	    StringBuilder hold = new StringBuilder();
		for(char letter : letters){
			for(char number : numbers){
				for(char number2 : numbers){
					for(char symbol : symbols){
						for(char symbol2 : symbols){
							hold.append(letter);
							hold.append(number);
							hold.append(number2);
							hold.append(symbol);
							hold.append(symbol2);
							comb.add(hold);
						}
					}
				}
			}
		}
		//case 2-------- 2 letters + 2 numbers + 1 symbol
		for(char letter : letters){
			for(char letter2 : letters){
				for(char number : numbers){
					for(char number2 : numbers){
						for(char symbol : symbols){
							hold.append(letter);
							hold.append(letter2);
							hold.append(number);
							hold.append(number2);
							hold.append(symbol);
							comb.add(hold);
						}
					}
				}
				
			}
		}
		//case 3 --------- 2 letters + 1 number + 2 symbols
		for(char letter : letters){
			for(char letter2 : letters){
				for(char number : numbers){
						for(char symbol : symbols){
							for(char symbol2 : symbols){
								hold.append(letter);
								hold.append(letter2);
								hold.append(number);
								hold.append(symbol);
								hold.append(symbol2);
								comb.add(hold);
							}
						}
				}
				
			}
		}
		//case 4--------- 3 letters + 1 number + 1 symbol
		for(char letter : letters){
			for(char letter2 : letters){
				for(char letter3 : letters){
					for(char number : numbers){
						for(char symbol : symbols){
							hold.append(letter);
							hold.append(letter2);
							hold.append(letter3);
							hold.append(number);
							hold.append(symbol);
							comb.add(hold);
						}
					}
				}
				
			}
		}
		//finally we pass each string in the hashset to permutation 
		for(int i = 0; i <= hold.length() - 5; ++i){
			permutation(hold.substring(i, i+5));
		}

	}

	
	
	public void permutation(String str) { 
	    permutation("", str);
	}
	public void permutation(String prefix, String str) {
	    int n = str.length();
	    if(n == 0){
	    	if(!search(prefix)){		//if search returns true, that means this is a good password, so it's time to write it to good_passwords.txt
	    		
	    		File file = new File("good_passwrods.txt");
	    		try {
	    			if(!file.exists()){
	    				file.createNewFile();
	    			}
	    			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
	    			BufferedWriter bw = new BufferedWriter(fw);
	    			bw.write(prefix);
	    			bw.newLine();
	    			bw.close();

	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	
	    }
	    else{
	        for (int i = 0; i < n; i++){
	            permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
	        }
	    }
	}
	
	//returns true if the word is not found in the dictionary trie, aka, it's a good password, returns false if it's found,aka, bad password
	public boolean search(String input) {
		//TO DO
		boolean found = false;
		int number;
		int nextNum;
		String str = "";
		for(int i = 0; i < input.length(); i++){
			number = (int)input.charAt(i);
			// if the current character is not a letter or a number that could substitute a letter, we simply don't check the rest of it
			if((number <= 122 && number >= 97) || 
					number == 48 || // 0
					number == 49 || // 1
					number == 51 || // 3
					number == 52 || // 4
					number == 53 || // 5
					number == 55){	// 7		
				str += input.charAt(i);
				if(i != input.length() - 1){			//we need to know if the next char is also a letter or valid number so we know where to stop and pass the inner word to searchInTrie(str)
					nextNum = (int)input.charAt(i+1);
					if(nextNum == 33 ||	     // !				// if next char is not a letter or valid number, we stop and pass the inner word to searchInTrie(str) then set str = "" then continue to search 
							nextNum == 64 || // @
							nextNum == 36 || // $
							nextNum == 37 || // %
							nextNum == 38 || // &
							nextNum == 42 || // *
							nextNum == 50 || // 2
							nextNum == 54 || // 6
							nextNum == 56 || // 8
							nextNum == 57){  // 9

						found = searchInTrie(str);
						if(found == true){
							return true;
						}
						str = "";
					}
				}
				else{
					found = searchInTrie(str);
					if(found == true){
						return true;
					}
				}
			}
		}
		return found;
	}
	//returns true if the word is in the trie, false if not 
	public boolean searchInTrie(String s){
		Node currentNode = rootNode;				//start the search from rootNode, move horizontally, if found go down if not return 
		int i = 0;
		int nodeLength = 0;
		boolean flag = true;
		while(flag){
			if(i < s.length()){
				int value = (int)s.charAt(i);				//get the ascii number of the current char so we know if it's a number or a letter
				while(currentNode != null){
					//first we need to know if we are going to compare letters or numbers(currentNode.subNum)
					if(value >= 97 && value <=122){ 			//when the current char is a letter, we only check node.value						
						if(currentNode.value == s.charAt(i)){		//see if the letter is in this level 
							if(currentNode.end == true){			// if this is end of the word in the trie, then we check if nodeLength == i + 1
								if(nodeLength == i){				//if this is end of the word and nodeLength also is the same as i, then we have found the word
									return true;
								}
							}
							currentNode = currentNode.child;		// if the letter is found, we go to next level and check the next char
							if(i != s.length()-1){
								++nodeLength;
								++i;
							}
							
							if(i < s.length()){
								value = (int)s.charAt(i);
								continue;
							}
							else{
								return false;
							}
						}
						currentNode = currentNode.next;				// if not, we go to nextNode and check again
						if(currentNode == null){					//if we finish searching the linked list and the letter is not there then return false
							return false;
						}
						continue;
					}
					else{										//when the current char is a number, then we check node.subNum
						if((currentNode.subNum) == (s.charAt(i))){
							if(currentNode.end == true){
								if(nodeLength == i){
									return true;
								}
							}
							currentNode = currentNode.child;
							if(i != s.length()-1){
								++nodeLength;
								++i;
							}

							if(i < s.length()){
								value = (int)s.charAt(i);
								continue;
							}
						}
						currentNode = currentNode.next;
						if(currentNode == null){
							return false;
						}
						continue;
					
					}
				}
				++i;
			}
			else{
				flag = false;
			}
		}
		return false;
	}
	
	
	

	//WE NEED TO GENERATE A TRIE THAT HAS ALL THE WORDS FROM DICTIONARY.TXT - BAD PASSWORDS
	 public void generateDicTrie() throws IOException{
		//TO DO
		Hashtable<Character, Character> containSub = new Hashtable<Character, Character>();
		containSub.put('a', '4');
		containSub.put('A', '4');
		
		containSub.put('e', '3');
		containSub.put('E', '3');
		
		containSub.put('i', '1');
		containSub.put('I', '1');
		
		containSub.put('l', '1');
		containSub.put('L', '1');
		
		containSub.put('o', '0');
		containSub.put('O', '0');
		
		containSub.put('s', '5');
		containSub.put('S', '5');
		
		containSub.put('t', '7');
		containSub.put('T', '7');
		
		FileReader in = new FileReader("dictionary.txt");
		BufferedReader read = new BufferedReader(in);
		String line;
		//I PUT line != "" FOR THIS WHILE LOOP CONDITION, THIS ONE FUCKING LINE MADE ME STAY UP UNTIL NOW! CURRENT TIME: 4:28 am! SON OF A BITCH!
		while((line = read.readLine()) != null){
			if( line.length() <= 5 && !line.isEmpty() ){			//we only add the word if it's less than 5 characters
							add(line, containSub);					//PASS THE WORD TO ADD
							writeToDic(line);				//call writeToDic() to write the word to my_dictionary.txt

			}
		}

		/*
		System.out.println(rootNode.value);
		while(rootNode.next != null){
			System.out.println(rootNode.next.value);
			rootNode = rootNode.next;
		}
		*/
		read.close();
	}

	

	// This method is called by generateTrie, and writes all the words in the trie to a file my_dictionary.txt
	// Note that, because our password can only be 5 characters, generateTrie ignores all wor�  ds that are more than 5 characters from dictionary.txt, which means that my_dictionary.txt does not contain all the words from that file
	public void writeToDic(String s) throws IOException{
		File file = new File("my_dictionary.txt");
		        try {
		            if(!file.exists()){
		            	file.createNewFile();
		            }
		            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(s);
					bw.newLine();
					bw.close();

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	}
		
		
	
	
	//ADD THE WORD FROM THE DICTIONARY, s is the word, here we only receive words that only have less than 5 characters.
	public void add(String s, Hashtable<Character, Character> containSub){
		//TO DO
		
		if(isEmpty){
			if(s.length() == 1){
				rootNode = new Node(s.charAt(0)); 					// SET UP ROOTNODE (letter, nextNode, childNode)
				if(containSub.containsKey(rootNode.value)){												//this determines if we define subNum
					rootNode.subNum = containSub.get(rootNode.value);      //////////////////////////////////////////////////////////////////
				}
				rootNode.end = true; 											// INDICATES THAT THIS IS THE END OF THE WORD
				isEmpty = false;
				return;
			}
			else{
				rootNode = new Node(s.charAt(0));
				if(containSub.containsKey(rootNode.value)){												//this determines if we define subNum
					rootNode.subNum = containSub.get(rootNode.value);      //////////////////////////////////////////////////////////////////
				}
				Node currentNode = rootNode;
				
				for(int i = 1; i < s.length(); ++i){							// ADD THE FIRST WORD
					currentNode.child = new Node(s.charAt(i));
					if(containSub.containsKey(currentNode.child.value)){												//this determines if we define subNum
						currentNode.child.subNum = containSub.get(currentNode.child.value);      //////////////////////////////////////////////////////////////////
					}
					currentNode = currentNode.child;
				}
				currentNode.end = true;											//SET END TO FALSE, END OF THE WORD
				
			}
			isEmpty = false; 	// SET isEmpty TO false NOW BECAUSE WE HAVE ALREADY SET UP THE ROOTNODE.
			return;
		}

		Node currentNode = rootNode;
		char c[] = s.toCharArray();	// an array of characters
		int i = 0;					// char array's index
		boolean keep_checking = true;
		while(keep_checking){
			while(currentNode != null){			// we check the linked list to see if the letter already exists
				if(currentNode.value == c[i]){			//if the letter is already there, we go down a level and check the next letter in the word.
					if(currentNode.child != null){
						if(i != c.length-1){
							++i;
							currentNode = currentNode.child;
							continue;
						}
						currentNode.end = true;
						return;
					}
					else{
						if(i != c.length-1){
							++i;
							currentNode.child = new Node(c[i]);
							currentNode = currentNode.child;
						}
						for(int j = i+1; j < c.length; j++){
							currentNode.child = new Node(c[i]);
						}
						currentNode.end = true;
						return;
					}
				}
				else{
					if(currentNode.next != null){
						currentNode = currentNode.next;
					}
					else{
						break;
					}
				}
			}
				currentNode.next = new Node(c[i]);
				currentNode = currentNode.next;
				if(containSub.containsKey(currentNode.value)){												//this determines if we define subNum
					currentNode.subNum = containSub.get(currentNode.value);      //////////////////////////////////////////////////////////////////
				}
				++i;
				if(i >= c.length){
					currentNode.end = true;
					return;
				}
				while(i < c.length){
					currentNode.child = new Node(c[i]);
					currentNode = currentNode.child;
					if(containSub.containsKey(currentNode.value)){												//this determines if we define subNum
						currentNode.subNum = containSub.get(currentNode.value);      //////////////////////////////////////////////////////////////////
					}
					++i;
					if(i == c.length){
						currentNode.end = true;	
						return;
					}
				}
		}
	}
	public class Node{
		Node next;
		Node child; // this is always the head
		char value;
		char subNum; // substituted number
		boolean end = false;

		
		public Node(char letter){
			this(letter, null, null);
		}
		
		public Node(char letter, Node nextNode, Node childNode){
			this.value = letter;// THE LETTER IN THE NODE
			this.next = nextNode;// NEXT NODE
			this.child = childNode; // CHILD NODE
		}
		

	}
}
