package Parse;



class Yylex implements Lexer, java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 65536;
	private final int YY_EOF = 65537;

	private int comment_count = 0;
	private int string_start = 0;
	private StringBuilder string_text;
	private ErrorMsg.ErrorMsg errorMsg;
	private void newline() {
		errorMsg.newline(yychar+1);
	}
	private void err(int pos, String s) {
  		errorMsg.error(pos,s);
	}
	private void err(String s) {
 		err(yychar,s);
	}
	private java_cup.runtime.Symbol tok(int kind, Object value) {
		if(kind==sym.STRING)
			return new java_cup.runtime.Symbol(kind, string_start, yychar, value);
    	return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
	}
	@Override
	public java_cup.runtime.Symbol next_token() throws Exception{
		return nextToken();
	}
	Yylex(java.io.InputStream s, ErrorMsg.ErrorMsg e) {
		this(s);
		errorMsg=e;
	}
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex(java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex(java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex() {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int STRING_IGNORE = 3;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int STRING_TEXT = 2;
	private final int yy_state_dtrans[] = {
		0,
		68,
		74,
		84
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private String yytext () {
		return (new String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		System.out.print(yy_error_string[code]);
		System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NOT_ACCEPT,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NOT_ACCEPT,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,65538,
"46:9,45,43,46,45,44,46:18,45,46,47,46:3,18,46,4,5,13,11,1,12,10,14,40:10,2," +
"3,16,15,17,46,50,51:26,6,48,7,49,42,46,32,34,37,31,24,21,41,23,20,41,35,26," +
"41,25,29,39,41,30,27,22,36,38,28,41,33,41,8,19,9,46:65410,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,118,
"0,1:2,2,1:10,3,4,1,5,6,1:2,7,8,1:2,9,1:7,10:17,1:6,11,1:9,12,1,13,14,1,15,1" +
"6,1,17,18,19,20,21,9,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39," +
"40,41,42,43,44,45,46,47,48,49,10,50,51,52,53,54,55,56,57,58")[0];

	private int yy_nxt[][] = unpackFromString(59,52,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,69,75,108,111,112,113" +
",108,114,78,108,81,115,108,116,108:3,117,108,22,108,23,24,25,70,23,26,23:3," +
"108,-1:67,27,-1:50,28,-1:50,29,-1:53,30,-1,31,-1:49,32,-1:56,108,33,108:3,3" +
"4,108:17,-1:8,108,-1:40,22,-1:54,24,-1:28,108:23,-1:8,108,-1:22,57,-1:2,58," +
"-1:14,77,-1:6,59,60,80,-1:45,65,-1:8,1,50:12,71,76,50:28,24,79,50:7,-1:20,1" +
"08:9,83,108:6,85,108:6,-1:8,108,-1:14,51,-1:80,54,-1:8,1,53:42,54,72,53:2,5" +
"5,56,53:3,-1:20,108:3,86,108:5,35,108:3,87,108:9,-1:8,108,-1:13,52,-1:78,82" +
",-1:31,108,36,108:21,-1:8,108,-1,61:5,62:2,61:34,62,-1:2,61:2,62,61,62:3,-1" +
":20,108:9,37,108:13,-1:8,108,-1:40,63,-1:31,108:10,38,108:12,-1:8,108,1,64:" +
"42,65,66,73,64:2,67,64:3,-1:20,108:5,95,108:17,-1:8,108,-1:20,108:4,96,108:" +
"18,-1:8,108,-1:20,108:19,97,108:3,-1:8,108,-1:20,108:11,39,108:11,-1:8,108," +
"-1:20,108:7,98,108:15,-1:8,108,-1:20,108:6,40,108:16,-1:8,108,-1:20,108:2,4" +
"1,108:20,-1:8,108,-1:20,99,108:22,-1:8,108,-1:20,108:10,100,108:12,-1:8,108" +
",-1:20,108:10,42,108:12,-1:8,108,-1:20,108:17,101,108:5,-1:8,108,-1:20,108:" +
"5,43,108:17,-1:8,108,-1:20,108:4,44,108:18,-1:8,108,-1:20,108:4,45,108:18,-" +
"1:8,108,-1:20,108:6,102,108:16,-1:8,108,-1:20,108:12,103,108:10,-1:8,108,-1" +
":20,108:2,105,108:20,-1:8,108,-1:20,108:4,46,108:18,-1:8,108,-1:20,108:13,4" +
"7,108:9,-1:8,108,-1:20,108:15,48,108:7,-1:8,108,-1:20,106,108:22,-1:8,108,-" +
"1:20,108:9,107,108:13,-1:8,108,-1:20,108:5,49,108:17,-1:8,108,-1:20,108:4,1" +
"10,108:18,-1:8,108,-1:20,108:12,104,108:10,-1:8,108,-1:20,108:5,88,89,108:1" +
"6,-1:8,108,-1:20,90,108:22,-1:8,108,-1:20,108:4,91,108:18,-1:8,108,-1:20,10" +
"8:3,92,108:19,-1:8,108,-1:20,108:10,93,108:12,-1:8,108,-1:20,108:10,109,108" +
":12,-1:8,108,-1:20,108:12,94,108:10,-1:8,108");

	public java_cup.runtime.Symbol nextToken ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

{
	if(comment_count>0)
		err("Unmatched Comment!");
	if(string_text!=null)
		err("Unfinished String!");
	return tok(sym.EOF, null);
}
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return tok(sym.COMMA, null);}
					case -3:
						break;
					case 3:
						{ return tok(sym.COLON, null);}
					case -4:
						break;
					case 4:
						{ return tok(sym.SEMICOLON, null);}
					case -5:
						break;
					case 5:
						{ return tok(sym.LPAREN, null);}
					case -6:
						break;
					case 6:
						{ return tok(sym.RPAREN, null);}
					case -7:
						break;
					case 7:
						{ return tok(sym.LBRACK, null);}
					case -8:
						break;
					case 8:
						{ return tok(sym.RBRACK, null);}
					case -9:
						break;
					case 9:
						{ return tok(sym.LBRACE, null);}
					case -10:
						break;
					case 10:
						{ return tok(sym.RBRACE, null);}
					case -11:
						break;
					case 11:
						{ return tok(sym.DOT, null);}
					case -12:
						break;
					case 12:
						{ return tok(sym.PLUS, null);}
					case -13:
						break;
					case 13:
						{ return tok(sym.MINUS, null);}
					case -14:
						break;
					case 14:
						{ return tok(sym.TIMES, null);}
					case -15:
						break;
					case 15:
						{ return tok(sym.DIVIDE, null);}
					case -16:
						break;
					case 16:
						{ return tok(sym.EQ, null);}
					case -17:
						break;
					case 17:
						{ return tok(sym.LT, null);}
					case -18:
						break;
					case 18:
						{ return tok(sym.GT, null);}
					case -19:
						break;
					case 19:
						{ return tok(sym.AND, null);}
					case -20:
						break;
					case 20:
						{ return tok(sym.OR, null);}
					case -21:
						break;
					case 21:
						{ return tok(sym.ID, yytext());}
					case -22:
						break;
					case 22:
						{ 
	return tok(sym.INT , new Integer(Integer.parseInt(yytext()))); }
					case -23:
						break;
					case 23:
						{ err("Illegal character: <" + yytext() + ">");}
					case -24:
						break;
					case 24:
						{ newline(); }
					case -25:
						break;
					case 25:
						{ }
					case -26:
						break;
					case 26:
						{ 
	string_text = new StringBuilder(); 
	string_start = yychar+1; 
	yybegin(STRING_TEXT);
	}
					case -27:
						break;
					case 27:
						{ return tok(sym.ASSIGN, null);}
					case -28:
						break;
					case 28:
						{ err("Unmatched Comment!"); }
					case -29:
						break;
					case 29:
						{ yybegin(COMMENT); comment_count = comment_count + 1; }
					case -30:
						break;
					case 30:
						{ return tok(sym.LE, null);}
					case -31:
						break;
					case 31:
						{ return tok(sym.NEQ, null);}
					case -32:
						break;
					case 32:
						{ return tok(sym.GE, null);}
					case -33:
						break;
					case 33:
						{ return tok(sym.IF, null);}
					case -34:
						break;
					case 34:
						{ return tok(sym.IN, null);}
					case -35:
						break;
					case 35:
						{ return tok(sym.TO, null);}
					case -36:
						break;
					case 36:
						{ return tok(sym.OF, null);}
					case -37:
						break;
					case 37:
						{ return tok(sym.DO, null);}
					case -38:
						break;
					case 38:
						{ return tok(sym.FOR, null);}
					case -39:
						break;
					case 39:
						{ return tok(sym.END, null);}
					case -40:
						break;
					case 40:
						{ return tok(sym.NIL, null);}
					case -41:
						break;
					case 41:
						{ return tok(sym.LET, null);}
					case -42:
						break;
					case 42:
						{ return tok(sym.VAR, null);}
					case -43:
						break;
					case 43:
						{ return tok(sym.THEN, null);}
					case -44:
						break;
					case 44:
						{ return tok(sym.TYPE, null);}
					case -45:
						break;
					case 45:
						{ return tok(sym.ELSE, null);}
					case -46:
						break;
					case 46:
						{ return tok(sym.WHILE, null);}
					case -47:
						break;
					case 47:
						{ return tok(sym.ARRAY, null);}
					case -48:
						break;
					case 48:
						{ return tok(sym.BREAK, null);}
					case -49:
						break;
					case 49:
						{ return tok(sym.FUNCTION, null);}
					case -50:
						break;
					case 50:
						{ }
					case -51:
						break;
					case 51:
						{ 
	comment_count = comment_count - 1; 
	if (comment_count == 0)
    	yybegin(YYINITIAL);
    }
					case -52:
						break;
					case 52:
						{ comment_count = comment_count + 1; }
					case -53:
						break;
					case 53:
						{ string_text.append(yytext());}
					case -54:
						break;
					case 54:
						{
	System.out.println(yychar);
	err("Unfinished String! \"\\\" is expected!");
	newline();
	string_text = null;
	yybegin(YYINITIAL);
	}
					case -55:
						break;
					case 55:
						{ 
	String str = string_text.toString();
	string_text = null;
	yybegin(YYINITIAL);
	return tok(sym.STRING, str);
	}
					case -56:
						break;
					case 56:
						{ yybegin(STRING_IGNORE);}
					case -57:
						break;
					case 57:
						{ string_text.append("\t");}
					case -58:
						break;
					case 58:
						{ string_text.append("\n");}
					case -59:
						break;
					case 59:
						{ string_text.append("\"");}
					case -60:
						break;
					case 60:
						{ string_text.append("\\");}
					case -61:
						break;
					case 61:
						{ err(yytext()+" is not CONTROL CHARACTER!");}
					case -62:
						break;
					case 62:
						{ 
	int ch = (char)yytext().charAt(2)-64;
	string_text.append((char)ch);
	}
					case -63:
						break;
					case 63:
						{ 
	int ch = Integer.parseInt(yytext().substring(1));
	if (ch >= 128)
		err("Character \""+yytext()+"\" is OUT OF RANGE!");
	else
		string_text.append((char)ch);
	}
					case -64:
						break;
					case 64:
						{ err("<"+yytext()+"> is illagal!");}
					case -65:
						break;
					case 65:
						{ newline(); }
					case -66:
						break;
					case 66:
						{ }
					case -67:
						break;
					case 67:
						{ yybegin(STRING_TEXT);}
					case -68:
						break;
					case 69:
						{ return tok(sym.ID, yytext());}
					case -69:
						break;
					case 70:
						{ }
					case -70:
						break;
					case 71:
						{ }
					case -71:
						break;
					case 72:
						{ string_text.append(yytext());}
					case -72:
						break;
					case 73:
						{ }
					case -73:
						break;
					case 75:
						{ return tok(sym.ID, yytext());}
					case -74:
						break;
					case 76:
						{ }
					case -75:
						break;
					case 78:
						{ return tok(sym.ID, yytext());}
					case -76:
						break;
					case 79:
						{ }
					case -77:
						break;
					case 81:
						{ return tok(sym.ID, yytext());}
					case -78:
						break;
					case 83:
						{ return tok(sym.ID, yytext());}
					case -79:
						break;
					case 85:
						{ return tok(sym.ID, yytext());}
					case -80:
						break;
					case 86:
						{ return tok(sym.ID, yytext());}
					case -81:
						break;
					case 87:
						{ return tok(sym.ID, yytext());}
					case -82:
						break;
					case 88:
						{ return tok(sym.ID, yytext());}
					case -83:
						break;
					case 89:
						{ return tok(sym.ID, yytext());}
					case -84:
						break;
					case 90:
						{ return tok(sym.ID, yytext());}
					case -85:
						break;
					case 91:
						{ return tok(sym.ID, yytext());}
					case -86:
						break;
					case 92:
						{ return tok(sym.ID, yytext());}
					case -87:
						break;
					case 93:
						{ return tok(sym.ID, yytext());}
					case -88:
						break;
					case 94:
						{ return tok(sym.ID, yytext());}
					case -89:
						break;
					case 95:
						{ return tok(sym.ID, yytext());}
					case -90:
						break;
					case 96:
						{ return tok(sym.ID, yytext());}
					case -91:
						break;
					case 97:
						{ return tok(sym.ID, yytext());}
					case -92:
						break;
					case 98:
						{ return tok(sym.ID, yytext());}
					case -93:
						break;
					case 99:
						{ return tok(sym.ID, yytext());}
					case -94:
						break;
					case 100:
						{ return tok(sym.ID, yytext());}
					case -95:
						break;
					case 101:
						{ return tok(sym.ID, yytext());}
					case -96:
						break;
					case 102:
						{ return tok(sym.ID, yytext());}
					case -97:
						break;
					case 103:
						{ return tok(sym.ID, yytext());}
					case -98:
						break;
					case 104:
						{ return tok(sym.ID, yytext());}
					case -99:
						break;
					case 105:
						{ return tok(sym.ID, yytext());}
					case -100:
						break;
					case 106:
						{ return tok(sym.ID, yytext());}
					case -101:
						break;
					case 107:
						{ return tok(sym.ID, yytext());}
					case -102:
						break;
					case 108:
						{ return tok(sym.ID, yytext());}
					case -103:
						break;
					case 109:
						{ return tok(sym.ID, yytext());}
					case -104:
						break;
					case 110:
						{ return tok(sym.ID, yytext());}
					case -105:
						break;
					case 111:
						{ return tok(sym.ID, yytext());}
					case -106:
						break;
					case 112:
						{ return tok(sym.ID, yytext());}
					case -107:
						break;
					case 113:
						{ return tok(sym.ID, yytext());}
					case -108:
						break;
					case 114:
						{ return tok(sym.ID, yytext());}
					case -109:
						break;
					case 115:
						{ return tok(sym.ID, yytext());}
					case -110:
						break;
					case 116:
						{ return tok(sym.ID, yytext());}
					case -111:
						break;
					case 117:
						{ return tok(sym.ID, yytext());}
					case -112:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
