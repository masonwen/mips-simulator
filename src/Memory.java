import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;


public class Memory {
	
	static private Memory instance;			// ��������
	
	/**
	 * ��������
	 * @return Ψһ��Memory����
	 */
	static public Memory getInstance() {
		
		if(instance == null) {
			instance = new Memory();
		}
		return instance;
	}
	
	static private void log(String s) {
		
		//System.out.println("[Memory]"+s);
	}

	
	
	static public final int
		MEMSIZE  = 4194304,
		DISPMODE = 0x1FFFFC,
			TEXTMODE    = 0,
			GRAPHICMODE = 1,
		DISPADDR = 0x200000,
		TEXTADDR = 0x380000,
		KEYBADDR = 0x390000;
	
	byte[] mem;
	
	public Memory() {
		
		mem = new byte[MEMSIZE];
		log("[Memory]Memory created.");
	}
	
	/**
	 * ��ָ���ڴ��ַ��32bit����
	 * @param address	��ַ
	 * @return	��ַ����ֵ
	 */
	public int read(int address) {
		
		int v0 = (readByte(address+3) & 0xff) << 24;
		int v1 = (readByte(address+2) & 0xff) << 16;
		int v2 = (readByte(address+1) & 0xff) << 8;
		int v3 = (readByte(address)   & 0xff);
		int v  = v0+v1+v2+v3;
		
		log("[read]"+Integer.toHexString(address)+"h=>"+Integer.toHexString(v) + "h");
		
		return v;
	}
	
	/**
	 * ��ָ���ڴ��һ��byte
	 * @param address	��ַ
	 * @return ���ʴ���ֵ
	 */
	public byte readByte(int address) {
		
		if(address < 0 || address >= MEMSIZE) {
			log("[readByte]Error: invalid memory address: " + Integer.toHexString(address) + "h");
			return 0;
		}
		
		return mem[address];
	}
	
	/**
	 * ��ָ���ڴ��ַдһ��32bit����
	 * @param address	��ַ
	 * @param value		Ҫд�������
	 */
	public void write(int address, int value) {
		
		if(address < 0 || address > MEMSIZE-4) {
			log("[write]Error: invalid memory address: " + Integer.toHexString(address) + "h");
			return;
		}

		writeByte(address  , (byte)(value)       );
		writeByte(address+1, (byte)(value >>> 8 ));
		writeByte(address+2, (byte)(value >>> 16));
		writeByte(address+3, (byte)(value >>> 24));
		
		log("[write]"+Integer.toHexString(address)+"h<="+Integer.toHexString(value) + "h");
	}
	
	/**
	 * ��ָ���ڴ�дһ��byte
	 * @param address	��ַ
	 * @param value     ֵ
	 */
	public void writeByte(int address, byte value) {
		
		if(address < 0 || address >= MEMSIZE) {
			log("[writeByte]Error: invalid memory address: " + Integer.toHexString(address) + "h");
			return;
		}
		
		mem[address] = value;
	}
	
	/**
	 * ���ı�ģʽ�Դ��ָ��λ��дһ���ַ�
	 * @param x ������
	 * @param y ������
	 * @param c �ַ�
	 */
	public void writeTextMode(int x, int y, char c) {
		writeByte(TEXTADDR + y*Display.TEXTXNUM + x, (byte)c);
	}
	
	/**
	 * ��ͼ��ģʽ�Դ��ָ��λ�û�һ����
	 * @param x ������
	 * @param y ������
	 * @param r ����Ϊ0/1
	 * @param g ����Ϊ0/1
	 * @param b ����Ϊ0/1
	 */
	public void writeGraphicMode(int x, int y, int r, int g, int b) {
		int addr = DISPADDR + (y*Display.DISPWIDTH + x)/2;
		byte t = readByte(addr);
		if(x % 2 == 0) {
			t &= 0x0F;
			if(r != 0) t |= 0x80;
			if(g != 0) t |= 0x40;
			if(b != 0) t |= 0x20;
		} else {
			t &= 0xF0;
			if(r != 0) t |= 0x08;
			if(g != 0) t |= 0x04;
			if(b != 0) t |= 0x02;
		}
		writeByte(addr, t);
	}
	
	/**
	 * ��ȡ���̰���ֵ
	 * @return ���������ڰ���ĳ����������ASCII�룬�ٴε��ý�����0�����û�м������£�ֱ�ӷ���0��
	 */
	public char getKeyboardKey() {
		
		char c = (char)readByte(KEYBADDR);
		writeByte(KEYBADDR, (byte) 0);
		
		log("[getKeyboardKey]=>" + c);
		
		return c;
	}
	
	/**
	 * ���ļ������ڴ档�ļ���ͷ��4byteָ����Ҫ����ĵ�ַ��
	 * @param fileName
	 * @throws IOException 
	 */
	public void load(String fileName) throws IOException {

		DataInputStream in = new DataInputStream(new FileInputStream(fileName));
		int addr = in.readInt();
		int data = 0;
		
		try {
			while (true) {
				data = in.readInt();
				write(addr, data);
				addr += 4;
			}
		} catch (EOFException e) {
			in.close();
			return;
		}
	}
	
	/**
	 * ��address��ʼ���ڴ��ַ��ʼ��һ���ַ�����������β��\0
	 * @param address ��ʼ��ַ
	 * @return �������ַ�����
	 */
	public String readString(int address) {
		
		String s = "";
		for(int a = address; readByte(a) != 0; a ++) {
			s += (char)readByte(a);
		}
		
		log("[readString]"+Integer.toHexString(address)+"h=>"+s);
		
		return s;
	}
	
	/**
	 * ��addressд��һ���ַ����������ĩβ����\0���������д��һ��\0
	 * @param address ��ַ
	 * @param data    Ҫд���ַ���
	 */
	public void writeString(int address, String data) {
		
		byte[] s = data.getBytes();
		int i;
		
		for(i = 0; i < s.length; i ++) {
			writeByte(address+i, s[i]);
		}
		
		if(s[i-1] != 0) {
			writeByte(address+i, (byte)0);
			log("[writeString]added 0 at the end.");
		}
		
		log("[writeString]"+Integer.toHexString(address)+"h<="+data);
		
		return;
	}

}
