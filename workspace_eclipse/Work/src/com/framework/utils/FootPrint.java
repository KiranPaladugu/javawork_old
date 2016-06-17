package com.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public abstract class FootPrint {
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static class HardwareAddressLookup {		
		public static String getMac() {
			String out = null;
			try {
				Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
				if (ifs != null) {
					while (ifs.hasMoreElements()) {
						NetworkInterface iface = ifs.nextElement();
						byte[] hardware = iface.getHardwareAddress();
						if (hardware != null && hardware.length == 6 && hardware[1] != (byte) 0xff) {
							out = append(new StringBuilder(36), hardware).toString();
							break;
						}
					}
				}
			} catch (SocketException ex) {
			}
			return out;
		}
		@SuppressWarnings("unused")
		public static String[] getAllMacAdresses() {
			String[] out = null;			
			try {
				Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();				
				if (ifs != null) {
					while (ifs.hasMoreElements()) {
						NetworkInterface iface = ifs.nextElement();
						byte[] hardware = iface.getHardwareAddress();
						if (hardware != null && hardware.length == 6 && hardware[1] != (byte) 0xff) {
							//out = append(new StringBuilder(36), hardware).toString();
							break;
						}
					}
				}
			} catch (SocketException ex) {
			}
			return out;
		}
	}

	private static String macAddress;

	public static String getFootPrint() {
		macAddress = HardwareAddressLookup.getMac();
		if (macAddress == null) {
			Process p = null;
			BufferedReader in = null;
			try {
				String osname = System.getProperty("os.name", "");
				if (osname.startsWith("Windows")) {
					p = Runtime.getRuntime().exec(new String[] { "ipconfig", "/all" }, null);
				} else if (osname.startsWith("Solaris") || osname.startsWith("SunOS")) {
					String hostName = getFirstLineOfCommand("uname", "-n");
					if (hostName != null) {
						p = Runtime.getRuntime().exec(new String[] { "/usr/sbin/arp", hostName }, null);
					}
				} else if (new File("/usr/sbin/lanscan").exists()) {
					p = Runtime.getRuntime().exec(new String[] { "/usr/sbin/lanscan" }, null);
				} else if (new File("/sbin/ifconfig").exists()) {
					p = Runtime.getRuntime().exec(new String[] { "/sbin/ifconfig", "-a" }, null);
				}

				if (p != null) {
					in = new BufferedReader(new InputStreamReader(p.getInputStream()), 128);
					String l = null;
					while ((l = in.readLine()) != null) {
						macAddress = parse(l);
						if (macAddress != null && parseShort(macAddress) != 0xff) {
							break;
						}
					}
				}

			} catch (SecurityException ex) {
			} catch (IOException ex) {
			} finally {
				if (p != null) {
					if (in != null) {
						try {
							in.close();
						} catch (IOException ex) {
						}
					}
					try {
						p.getErrorStream().close();
					} catch (IOException ex) {
					}
					try {
						p.getOutputStream().close();
					} catch (IOException ex) {
					}
					p.destroy();
				}
			}
		}
		if (macAddress.trim().equals("")) {
			return null;
		}
		return macAddress.trim().toUpperCase();
	}

	private static String getFirstLineOfCommand(String... commands) throws IOException {
		Process p = null;
		BufferedReader reader = null;
		try {
			p = Runtime.getRuntime().exec(commands);
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 128);
			return reader.readLine();
		} finally {
			if (p != null) {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException ex) {
					}
				}
				try {
					p.getErrorStream().close();
				} catch (IOException ex) {
				}
				try {
					p.getOutputStream().close();
				} catch (IOException ex) {
				}
				p.destroy();
			}
		}
	}

	private static String parse(String in) {
		String out = in;
		int hexStart = out.indexOf("0x");
		if (hexStart != -1 && out.indexOf("ETHER") != -1) {
			int hexEnd = out.indexOf(' ', hexStart);
			if (hexEnd > hexStart + 2) {
				out = out.substring(hexStart, hexEnd);
			}
		} else {
			int octets = 0;
			int lastIndex, old, end;
			if (out.indexOf('-') > -1) {
				out = out.replace('-', ':');
			}
			lastIndex = out.lastIndexOf(':');
			if (lastIndex > out.length() - 2) {
				out = null;
			} else {
				end = Math.min(out.length(), lastIndex + 3);
				++octets;
				old = lastIndex;
				while (octets != 5 && lastIndex != -1 && lastIndex > 1) {
					lastIndex = out.lastIndexOf(':', --lastIndex);
					if (old - lastIndex == 3 || old - lastIndex == 2) {
						++octets;
						old = lastIndex;
					}
				}
				if (octets == 5 && lastIndex > 1) {
					out = out.substring(lastIndex - 2, end).trim();
				} else {
					out = null;
				}
			}
		}

		if (out != null && out.startsWith("0x")) {
			out = out.substring(2);
		}
		return out;
	}

	private static Appendable append(Appendable a, byte[] bytes) {
		try {
			for (byte b : bytes) {
				a.append(DIGITS[(byte) ((b & 0xF0) >> 4)]);
				a.append(DIGITS[(byte) (b & 0x0F)]);
			}
		} catch (IOException ex) {
		}
		return a;
	}

	private static short parseShort(String s) {
		short out = 0;
		byte shifts = 0;
		char c;
		for (int i = 0; i < s.length() && shifts < 4; i++) {
			c = s.charAt(i);
			if ((c > 47) && (c < 58)) {
				++shifts;
				out <<= 4;
				out |= c - 48;
			} else if ((c > 64) && (c < 71)) {
				++shifts;
				out <<= 4;
				out |= c - 55;
			} else if ((c > 96) && (c < 103)) {
				++shifts;
				out <<= 4;
				out |= c - 87;
			}
		}
		return out;
	}
	
	public static boolean isValidFootPrint(String footPrint){
		boolean flag = false;
		try {
			Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
			if (ifs != null) {
				while (ifs.hasMoreElements()) {
					NetworkInterface iface = ifs.nextElement();
					byte[] hardware = iface.getHardwareAddress();
					if (hardware != null && hardware.length == 6 && hardware[1] != (byte) 0xff) {
						String fingerPrint = append(new StringBuilder(36), hardware).toString();
						if(fingerPrint.equalsIgnoreCase(footPrint)){
							flag=true;
							break;
						}						
					}
				}
			}
		} catch (SocketException ex) {
		}
		return flag;
	}
}
