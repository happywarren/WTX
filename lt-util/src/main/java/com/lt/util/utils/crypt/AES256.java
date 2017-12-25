package com.lt.util.utils.crypt;



public class AES256 {
	private final static String  AES_KEY = "CAINIU-ZJ-LUCKIN-COM.LTD";
	
	public static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	public int traceLevel = 0;

	public String traceInfo = "";

	public static final int ROUNDS = 14, // AES has 10-14 rounds
			BLOCK_SIZE = 16, // AES uses 128-bit (16 byte) key
			KEY_LENGTH = 32; // AES uses 128/192/256-bit (16/24/32 byte) key
	int numRounds;
	byte[][] Ke;
	byte[][] Kd;

	static final byte[] S = { 99, 124, 119, 123, -14, 107, 111, -59, 48, 1,
			103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16,
			-83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63,
			-9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24,
			-106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44,
			26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47,
			0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48,
			-17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88,
			81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1,
			-13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61,
			100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18,
			-72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62,
			-45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43,
			78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28,
			-90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62,
			-75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31,
			-8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85,
			40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15,
			-80, 84, -69, 22 };

	static final byte[] Si = { 82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93,
			-98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121,
			52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62,
			35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40,
			-39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10,
			100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110,
			108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115,
			-99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5,
			-72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67,
			3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105,
			-14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83,
			53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113,
			29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62,
			75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31,
			-35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95,
			96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55,
			-100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60,
			-125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105,
			20, 99, 85, 33, 12, 125 };

	static final byte[] rcon = { 0, 1, 2, 4, 8, 16, 32, 64, -128, 27, 54, 108,
			-40, -85, 77, -102, 47, 94, -68, 99, -58, -105, 53, 106, -44, -77,
			125, -6, -17, -59, -111 };

	public static final int COL_SIZE = 4, NUM_COLS = BLOCK_SIZE / COL_SIZE,
			ROOT = 0x11B;

	static final int[] row_shift = { 0, 1, 2, 3 };

	static final int[] alog = new int[256];

	static final int[] log = new int[256];

	static {
		int i, j;
		alog[0] = 1;
		for (i = 1; i < 256; i++) {
			j = (alog[i - 1] << 1) ^ alog[i - 1];
			if ((j & 0x100) != 0)
				j ^= ROOT;
			alog[i] = j;
		}
		for (i = 1; i < 255; i++)
			log[alog[i]] = i;
	}

	public static int getRounds(int keySize) {
		switch (keySize) {
		case 16: // 16 byte = 128 bit key
			return 10;
		case 24: // 24 byte = 192 bit key
			return 12;
		default: // 32 byte = 256 bit key
			return 14;
		}
	}

	static final int mul(int a, int b) {
		return (a != 0 && b != 0) ? alog[(log[a & 0xFF] + log[b & 0xFF]) % 255]
				: 0;
	}

	public byte[] encrypt(byte[] plain) {

		byte[] a = new byte[BLOCK_SIZE];
		byte[] ta = new byte[BLOCK_SIZE];
		byte[] Ker;

		int i, k, row, col;
		traceInfo = "";
		if (traceLevel > 0)
			traceInfo = "encryptAES(" + AES256Util.toHEX1(plain) + ")";

		if (plain == null)
			throw new IllegalArgumentException("Empty plaintext");
		if (plain.length != BLOCK_SIZE)
			throw new IllegalArgumentException("Incorrect plaintext length");

		Ker = Ke[0];
		for (i = 0; i < BLOCK_SIZE; i++)
			a[i] = (byte) (plain[i] ^ Ker[i]);
		if (traceLevel > 2)
			traceInfo += "n  R0 (Key = " + AES256Util.toHEX1(Ker) + ")ntAK = "
					+ AES256Util.toHEX1(a);
		else if (traceLevel > 1)
			traceInfo += "n  R0 (Key = " + AES256Util.toHEX1(Ker) + ")t = "
					+ AES256Util.toHEX1(a);

		for (int r = 1; r < numRounds; r++) {
			Ker = Ke[r];
			if (traceLevel > 1)
				traceInfo += "n  R" + r + " (Key = " + AES256Util.toHEX1(Ker) + ")t";

			for (i = 0; i < BLOCK_SIZE; i++)
				ta[i] = S[a[i] & 0xFF];
			if (traceLevel > 2)
				traceInfo += "ntSB = " + AES256Util.toHEX1(ta);

			for (i = 0; i < BLOCK_SIZE; i++) {
				row = i % COL_SIZE;
				k = (i + (row_shift[row] * COL_SIZE)) % BLOCK_SIZE;
				a[i] = ta[k];
			}
			if (traceLevel > 2)
				traceInfo += "ntSR = " + AES256Util.toHEX1(a);

			for (col = 0; col < NUM_COLS; col++) {
				i = col * COL_SIZE;
				ta[i] = (byte) (mul(2, a[i]) ^ mul(3, a[i + 1]) ^ a[i + 2] ^ a[i + 3]);
				ta[i + 1] = (byte) (a[i] ^ mul(2, a[i + 1]) ^ mul(3, a[i + 2]) ^ a[i + 3]);
				ta[i + 2] = (byte) (a[i] ^ a[i + 1] ^ mul(2, a[i + 2]) ^ mul(3,
						a[i + 3]));
				ta[i + 3] = (byte) (mul(3, a[i]) ^ a[i + 1] ^ a[i + 2] ^ mul(2,
						a[i + 3]));
			}
			if (traceLevel > 2)
				traceInfo += "ntMC = " + AES256Util.toHEX1(ta);

			for (i = 0; i < BLOCK_SIZE; i++)
				a[i] = (byte) (ta[i] ^ Ker[i]);
			if (traceLevel > 2)
				traceInfo += "ntAK";
			if (traceLevel > 1)
				traceInfo += " = " + AES256Util.toHEX1(a);
		}

		Ker = Ke[numRounds];
		if (traceLevel > 1)
			traceInfo += "n  R" + numRounds + " (Key = " + AES256Util.toHEX1(Ker)
					+ ")t";

		for (i = 0; i < BLOCK_SIZE; i++)
			a[i] = S[a[i] & 0xFF];

		if (traceLevel > 2)
			traceInfo += "ntSB = " + AES256Util.toHEX1(a);

		for (i = 0; i < BLOCK_SIZE; i++) {
			row = i % COL_SIZE;
			k = (i + (row_shift[row] * COL_SIZE)) % BLOCK_SIZE; // get shifted
																// byte index
			ta[i] = a[k];
		}
		if (traceLevel > 2)
			traceInfo += "ntSR = " + AES256Util.toHEX1(a);

		for (i = 0; i < BLOCK_SIZE; i++)
			a[i] = (byte) (ta[i] ^ Ker[i]);
		if (traceLevel > 2)
			traceInfo += "ntAK";
		if (traceLevel > 1)
			traceInfo += " = " + AES256Util.toHEX1(a) + "n";
		if (traceLevel > 0)
			traceInfo += " = " + AES256Util.toHEX1(a) + "n";
		return (a);
	}

	public byte[] decrypt(byte[] cipher) {

		byte[] a = new byte[BLOCK_SIZE];
		byte[] ta = new byte[BLOCK_SIZE];
		byte[] Kdr;

		int i, k, row, col;
		traceInfo = "";
		if (traceLevel > 0)
			traceInfo = "decryptAES(" + AES256Util.toHEX1(cipher) + ")";

		if (cipher == null)
			throw new IllegalArgumentException("Empty ciphertext");
		if (cipher.length != BLOCK_SIZE)
			throw new IllegalArgumentException("Incorrect ciphertext length");

		Kdr = Kd[0];
		for (i = 0; i < BLOCK_SIZE; i++)
			a[i] = (byte) (cipher[i] ^ Kdr[i]);
		if (traceLevel > 2)
			traceInfo += "n  R0 (Key = " + AES256Util.toHEX1(Kdr) + ")nt AK = "
					+ AES256Util.toHEX1(a);
		else if (traceLevel > 1)
			traceInfo += "n  R0 (Key = " + AES256Util.toHEX1(Kdr) + ")t = "
					+ AES256Util.toHEX1(a);

		for (int r = 1; r < numRounds; r++) {
			Kdr = Kd[r];
			if (traceLevel > 1)
				traceInfo += "n  R" + r + " (Key = " + AES256Util.toHEX1(Kdr) + ")t";

			for (i = 0; i < BLOCK_SIZE; i++) {
				row = i % COL_SIZE;

				k = (i + BLOCK_SIZE - (row_shift[row] * COL_SIZE)) % BLOCK_SIZE;
				ta[i] = a[k];
			}
			if (traceLevel > 2)
				traceInfo += "ntISR = " + AES256Util.toHEX1(ta);

			for (i = 0; i < BLOCK_SIZE; i++)
				a[i] = Si[ta[i] & 0xFF];
			if (traceLevel > 2)
				traceInfo += "ntISB = " + AES256Util.toHEX1(a);

			for (i = 0; i < BLOCK_SIZE; i++)
				ta[i] = (byte) (a[i] ^ Kdr[i]);
			if (traceLevel > 2)
				traceInfo += "nt AK = " + AES256Util.toHEX1(ta);

			for (col = 0; col < NUM_COLS; col++) {
				i = col * COL_SIZE;
				a[i] = (byte) (mul(0x0e, ta[i]) ^ mul(0x0b, ta[i + 1])
						^ mul(0x0d, ta[i + 2]) ^ mul(0x09, ta[i + 3]));
				a[i + 1] = (byte) (mul(0x09, ta[i]) ^ mul(0x0e, ta[i + 1])
						^ mul(0x0b, ta[i + 2]) ^ mul(0x0d, ta[i + 3]));
				a[i + 2] = (byte) (mul(0x0d, ta[i]) ^ mul(0x09, ta[i + 1])
						^ mul(0x0e, ta[i + 2]) ^ mul(0x0b, ta[i + 3]));
				a[i + 3] = (byte) (mul(0x0b, ta[i]) ^ mul(0x0d, ta[i + 1])
						^ mul(0x09, ta[i + 2]) ^ mul(0x0e, ta[i + 3]));
			}
			if (traceLevel > 2)
				traceInfo += "ntIMC";
			if (traceLevel > 1)
				traceInfo += " = " + AES256Util.toHEX1(a);
		}

		Kdr = Kd[numRounds];
		if (traceLevel > 1)
			traceInfo += "n  R" + numRounds + " (Key = " + AES256Util.toHEX1(Kdr)
					+ ")t";

		for (i = 0; i < BLOCK_SIZE; i++) {
			row = i % COL_SIZE;

			k = (i + BLOCK_SIZE - (row_shift[row] * COL_SIZE)) % BLOCK_SIZE;
			ta[i] = a[k];
		}
		if (traceLevel > 2)
			traceInfo += "ntISR = " + AES256Util.toHEX1(a);

		for (i = 0; i < BLOCK_SIZE; i++)
			ta[i] = Si[ta[i] & 0xFF];
		if (traceLevel > 2)
			traceInfo += "ntISB = " + AES256Util.toHEX1(a);

		for (i = 0; i < BLOCK_SIZE; i++)
			a[i] = (byte) (ta[i] ^ Kdr[i]);
		if (traceLevel > 2)
			traceInfo += "nt AK";
		if (traceLevel > 1)
			traceInfo += " = " + AES256Util.toHEX1(a) + "n";
		if (traceLevel > 0)
			traceInfo += " = " + AES256Util.toHEX1(a) + "n";
		return (a);
	}

	public void setKey(byte[] key) {

		final int BC = BLOCK_SIZE / 4;

		final int Klen = key.length;
		// System.out.println("key 长度"+Klen);
		final int Nk = Klen / 4;

		int i, j, r;

		traceInfo = "";
		if (traceLevel > 0)
			traceInfo = "setKey(" + AES256Util.toHEX1(key) + ")n";

		if (key == null)
			throw new IllegalArgumentException("Empty key");
		if (!(key.length == 16 || key.length == 24 || key.length == 32))
			throw new IllegalArgumentException("Incorrect key length");
		numRounds = getRounds(Klen);
		final int ROUND_KEY_COUNT = (numRounds + 1) * BC;

		byte[] w0 = new byte[ROUND_KEY_COUNT];
		byte[] w1 = new byte[ROUND_KEY_COUNT];
		byte[] w2 = new byte[ROUND_KEY_COUNT];
		byte[] w3 = new byte[ROUND_KEY_COUNT];

		Ke = new byte[numRounds + 1][BLOCK_SIZE];
		Kd = new byte[numRounds + 1][BLOCK_SIZE];

		for (i = 0, j = 0; i < Nk; i++) {
			w0[i] = key[j++];
			w1[i] = key[j++];
			w2[i] = key[j++];
			w3[i] = key[j++];
		}

		byte t0, t1, t2, t3, old0;
		for (i = Nk; i < ROUND_KEY_COUNT; i++) {
			t0 = w0[i - 1];
			t1 = w1[i - 1];
			t2 = w2[i - 1];
			t3 = w3[i - 1];
			if (i % Nk == 0) {

				old0 = t0;
				t0 = (byte) (S[t1 & 0xFF] ^ rcon[i / Nk]);
				t1 = (byte) (S[t2 & 0xFF]);
				t2 = (byte) (S[t3 & 0xFF]);
				t3 = (byte) (S[old0 & 0xFF]);
			} else if ((Nk > 6) && (i % Nk == 4)) {

				t0 = S[t0 & 0xFF];
				t1 = S[t1 & 0xFF];
				t2 = S[t2 & 0xFF];
				t3 = S[t3 & 0xFF];
			}

			w0[i] = (byte) (w0[i - Nk] ^ t0);
			w1[i] = (byte) (w1[i - Nk] ^ t1);
			w2[i] = (byte) (w2[i - Nk] ^ t2);
			w3[i] = (byte) (w3[i - Nk] ^ t3);
		}

		for (r = 0, i = 0; r < numRounds + 1; r++) {
			for (j = 0; j < BC; j++) {
				Ke[r][4 * j] = w0[i];
				Ke[r][4 * j + 1] = w1[i];
				Ke[r][4 * j + 2] = w2[i];
				Ke[r][4 * j + 3] = w3[i];
				Kd[numRounds - r][4 * j] = w0[i];
				Kd[numRounds - r][4 * j + 1] = w1[i];
				Kd[numRounds - r][4 * j + 2] = w2[i];
				Kd[numRounds - r][4 * j + 3] = w3[i];
				i++;
			}
		}

		if (traceLevel > 3) {
			traceInfo += "  Encrypt Round keys:n";
			for (r = 0; r < numRounds + 1; r++)
				traceInfo += "  R" + r + "t = " + AES256Util.toHEX1(Ke[r]) + "n";
			traceInfo += "  Decrypt Round keys:n";
			for (r = 0; r < numRounds + 1; r++)
				traceInfo += "  R" + r + "t = " + AES256Util.toHEX1(Kd[r]) + "n";
		}
	}

	public static String static_byteArrayToString(byte[] data) {
		String res = "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int n = (int) data[i];
			if (n < 0)
				n += 256;
			sb.append((char) n);
		}
		res = sb.toString();
		return res;
	}

	public static byte[] static_stringToByteArray(String s) {
		byte[] temp = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			temp[i] = (byte) s.charAt(i);
		}
		return temp;
	}

	public static String static_intArrayToString(int[] t) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < t.length; i++) {
			sb.append((char) t[i]);
		}
		return sb.toString();
	}

	public String _cryptAll(String data, int mode) {
		AES256 aes = this;
		if (data.length() / 16 > ((int) data.length() / 16)) {
			int rest = data.length() - ((int) data.length() / 16) * 16;
			for (int i = 0; i < rest; i++)
				data += " ";
		}
		int nParts = (int) data.length() / 16;
		byte[] res = new byte[data.length()];
		String partStr = "";
		byte[] partByte = new byte[16];
		for (int p = 0; p < nParts; p++) {
			partStr = data.substring(p * 16, p * 16 + 16);
			partByte = static_stringToByteArray(partStr);
			if (mode == 1)
				partByte = aes.encrypt(partByte);
			if (mode == 2)
				partByte = aes.decrypt(partByte);
			for (int b = 0; b < 16; b++)
				res[p * 16 + b] = partByte[b];
		}
		return static_byteArrayToString(res);
	}

	public String Encrypt(String data) {
		return _cryptAll(data, 1);
	}

	public String Decrypt(String data) {
		return _cryptAll(data, 2);
	}

	public void setKey(String key) {

		setKey(static_stringToByteArray(key));
	}

	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}


	public static String getEnString(String plaintext) {
		AES256 aes = new AES256();
		aes.setKey(AES_KEY.getBytes());
		int length = plaintext.length();
		StringBuilder destr = new StringBuilder();
		String estr = null;
		if (length > 64)
			System.err.toString();
		for (int t = 0; t < 64; t += 16) {
			String enstr = plaintext.substring(t, t + 16);
			byte[] bytedata = aes.encrypt(enstr.getBytes());
			estr = (destr.append(AES256Util.toHEX1(bytedata)).toString());
		}
		return estr;
	}

	public static String getDeString(String encryptstr) {
		AES256 aes = new AES256();
		aes.setKey(AES_KEY.getBytes());
		int length = encryptstr.length();
		StringBuilder destr = new StringBuilder();
		String dstr = null;
		if (length > 128)
			System.err.toString();
		for (int t = 0; t < 128; t += 32) {
			String enstr = encryptstr.substring(t, t + 32);
			byte[] bytedata = aes.decrypt(AES256Util.hex2byte(enstr));
			dstr = (destr.append(AES256Util.toHEX1(bytedata))).toString();

		}
		return dstr;
	}

	public static String getDecode(String encryptstr) {
		StringBuilder destr = new StringBuilder();
		int length = encryptstr.length();
		String dstr = null;
		if (length > 128)
			System.err.toString();
		for (int t = 0; t < 128; t += 2) {
			String codestr = encryptstr.substring(t, t + 2);
			// int intdd= Integer.valueOf(codestr,16);
			char m = (char) ((int) Integer.valueOf(codestr, 16));

			dstr = destr.append(String.valueOf(m)).toString();

		}
		return dstr;
	}
	
	/**
	 * 测试
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

//		/* 要加密的字符串：用户ID|登陆时间|有效期分钟 */
//		String source = StringTools.getRandom(14) + "|18268198627|2015-04-02 14:38:59|60|" + StringTools.getRandom(14);
//		
//		String EnString = getEnString(source);
//		System.out.println("加密字符串 " + EnString);
//		String DeString = getDeString(EnString);
//		System.out.println("解密字符串 " + DeString);
//		String Decodstr = getDecode(DeString);
//		System.out.println("格式化字符串 " + Decodstr);
//		String[] resultToken = Decodstr.split("\\|");
//		
//		System.out.println(resultToken[0]);
//		System.out.println(resultToken[1]);
//		System.out.println(resultToken[2]);
//		System.out.println(resultToken[3]);
		
		String id = "123456";
		System.out.println("加密字符串 " + getEnString(id));
		
	
	}
}
