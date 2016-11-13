package com.desktopdirect.tcs;

public class Base64 {

//for base64 encoding
private static String base64_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
private static char[] map = 
	"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
//given a character, get it's index into the base64 character map
private static int b64_index(char c)
{
	if (c >= 'A' && c <= 'Z') {
		return c - 'A';
	} else if (c >= 'a' && c <= 'z') {
		return c - 'a' + 26;
	} else if (c >= '0' && c <= '9') {
		return c - '0' + 52;
	} else if (c == '+') {
		return 62;
	} else if (c == '/') {
		return 63;
	}

	return -1; //invalid or pad ('=') char
}

public static char[] encode(byte[] data)
{
	if (data == null) {
		return null;
	}

	//every 3 bytes of the original data is encoded into 4 characters.
	//output is always a multiple of 4 chars, padded if needed.
	char[] out = new char[((data.length + 2) / 3) * 4];

	int val, i, index;

	boolean quad = false;
	boolean trip = false;

	index = 0;
	for (i = 0; i < data.length; i += 3) {
		quad = false;
		trip = false;

		val = (0xFF & (int)data[i]);
		val <<= 8;
		if ((i+1) < data.length) {
			val |= (0xFF & (int)data[i+1]);
			trip = true;
		}
		val <<= 8;
		if ((i+2) < data.length) {
			val |= (0xFF & (int)data[i+2]);
			quad = true;
		}
		out[index+3] = map[(quad ? (val & 0x3F) : 64)];
		val >>= 6;
		out[index+2] = map[(trip ? (val & 0x3F) : 64)];
		val >>= 6;
		out[index+1] = map[val & 0x3F];
		val >>= 6;
		out[index+0] = map[val & 0x3F];

		index += 4;
	}

	return out;
}

public static byte[] decode(char[] data)
{
	int i;

	if (data == null) {
		return null;
	}

	//search data for and discount any illegal characters
	int templen = data.length;
	for (i = 0; i < data.length; i++) {
		if (b64_index(data[i]) < 0) {
			templen--;
		}
	}

	//calculate required length using templen:
	//3 bytes for every 4 valid base64 chars
	//plus 2 bytes if there are 3 extra base64 chars,
	//or plus 1 byte if there are 2 extra.
	int len = (templen / 4) * 3;
	if ((templen % 4) == 3) len += 2;
	if ((templen % 4) == 2) len += 1;

	byte[] out = new byte[len];

	int shift = 0, //number of bits stored in accum
	    accum = 0, //stored bits
	    index = 0, //current index in output buffer
	    value;

	//we now go through the entire array
	for (i = 0; i < data.length; i++) {
		if (index == len) {
			break; //should never happen, but let's be safe
		}
		value = b64_index(data[i]);
		if (value >= 0) {
			accum <<= 6;        //bits shift up by 6 each time through
			shift += 6;         //the loop, with new bits being put in
			accum |= value;     //at the bottom.

			if (shift >= 8) {   //whenever there are 8 or more shifted in,
				shift -= 8;     //write them out from the top, leaving any
                                //excess at the bottom for next iteration.
				out[index++] = (byte)((accum >> shift) & 0xFF);
			}
		} //else character was illegal - skip it
	}

	return out;
}
public static String bytes2str(byte[] bytes, int offset, int len) {
	int i;
	byte[] temp = new byte[1];
	String return_str = new String("");

	for (i = offset; i < offset + len; i++) {
		temp[0] = bytes[i];
		if (temp[0] == 0) {
			break;
		}
		return_str += new String(temp);
	}
	return return_str;
}
public static String base64_decode(String encoded_string) {
	int in_len = encoded_string.length();
	int i = 0;
	int j = 0;
	int in_ = 0;

	char[] char_array_4 = new char[4];
	char[] char_array_3 = new char[3];

	StringBuilder ret = new StringBuilder();

	while ((in_len--) != 0 && (encoded_string.charAt(in_) != '=')
			&& is_base64(encoded_string.charAt(in_))) {
		char_array_4[i++] = encoded_string.charAt(in_);
		in_++;
		if (i == 4) {
			for (i = 0; i < 4; i++) {
				char_array_4[i] = (char) find(base64_chars, char_array_4[i]);
			}

			char_array_3[0] = (char) ((char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4));
			char_array_3[1] = (char) (((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2));
			char_array_3[2] = (char) (((char_array_4[2] & 0x3) << 6) + char_array_4[3]);

			for (i = 0; (i < 3); i++) {
				ret.append(char_array_3[i]);
			}
			i = 0;
		}
	}

	if (i != 0) {
		for (j = i; j < 4; j++)
			char_array_4[j] = 0;

		for (j = 0; j < 4; j++) {
			char_array_4[j] = (char) find(base64_chars, char_array_4[j]);
		}

		char_array_3[0] = (char) ((char_array_4[0] << 2) + ((char_array_4[1] & 0x30) >> 4));
		char_array_3[1] = (char) (((char_array_4[1] & 0xf) << 4) + ((char_array_4[2] & 0x3c) >> 2));
		char_array_3[2] = (char) (((char_array_4[2] & 0x3) << 6) + char_array_4[3]);

		for (j = 0; (j < i - 1); j++) {
			ret.append(char_array_3[j]);
		}
	}
	return ret.toString();
}

static int find(String value, char c) {
	int size = value.length();
	for (int i = 0; i < size; i++) {
		if (value.charAt(i) == c) {
			return i;
		}
	}
	return -1;
}

static boolean is_base64(char c) {
	return (isalnum(c) || (c == '+') || (c == '/'));
}

private static boolean isalnum(char c) {
	return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z')
			|| (c >= 'A' && c <= 'Z') ? true : false;
}
} //end Base64 class

