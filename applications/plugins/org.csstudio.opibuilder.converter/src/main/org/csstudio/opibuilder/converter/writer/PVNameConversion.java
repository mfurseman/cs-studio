package org.csstudio.opibuilder.converter.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.csstudio.java.string.StringSplitter;

public class PVNameConversion {

	public static String convertPVName(String pvName) {

		if (pvName.startsWith("LOC")) {
			pvName = parseLocPV(pvName);
		} else if (pvName.startsWith("CALC")) {
			pvName = parseCalcPV(pvName);
		}

		return pvName;
	}

	private static String parseLocPV(String pvName) {
		try {
			String newName = pvName.replace("$(!W)", "$(DID)");
			newName = newName.replaceAll("\\x24\\x28\\x21[A-Z]{1}\\x29",
					"\\$(DID)");
			String[] parts = StringSplitter.splitIgnoreInQuotes(newName, '=',
					true);
			StringBuilder sb = new StringBuilder("loc://");
			sb.append(parts[0].substring(5));
			if (parts.length > 1) {
				String type = "";
				String initValue = parts[1];
				if (parts[1].startsWith("d:")) {
					type = "<VDouble>";
					initValue = parts[1].substring(2);
				} else if (parts[1].startsWith("i:")) {
					type = "<VDouble>";
					initValue = parts[1].substring(2);
				} else if (parts[1].startsWith("s:")) {
					type = "<VString>";
					initValue = "\"" + parts[1].substring(2) + "\"";
				} else if (parts[1].startsWith("e:")) { // Enumerated pv
					type = "<VEnum>";
					initValue = parts[1].substring(2);
					String[] ps = initValue.split(",");
					initValue = ps[0];
					for (int i = 1; i < ps.length; i++) {
						initValue += ",\"" + ps[i] + "\"";
					}
				}
				// re-appending type in contrast to previous comment
				sb.append(type);
				sb.append("(").append(initValue).append(")");
			}
			return sb.toString();

		} catch (Exception e) {
			return pvName;
		}
	}

	private static List<String> parseOperator(String op) {
		List<String> ops = new ArrayList<String>();
		if (op.startsWith("\\{")) {
			op = op.substring(2, op.length() - 2);
			StringTokenizer st = new StringTokenizer(op, "*/+-", true);
			while (st.hasMoreTokens()) {
				String t = st.nextToken();
				// Ignore the placeholders (A, B ...)
				if (!StringUtils.isAllUpperCase(t)) {
					ops.add(t);
				}
			}
		} else if (op.equals("sum")) {
			// ... add plenty of +s ... 
			ops.add("+");
			ops.add("+");
			ops.add("+");
		}
		return ops;
	}

	/**
	 * Return variables provided in arguments to CALC pv as a List of Strings
	 * 
	 * @param argString
	 *            - the contents of CALC parentheses
	 * @return List<String> of variables
	 */
	private static List<String> parseArguments(String argString) {
		List<String> arguments = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(argString, ",", false);
		while (st.hasMoreTokens()) {
			String t = st.nextToken().trim();
			if (isPvString(t)) {
				arguments.add("pv(\"" + t + "\")");
			} else {
				arguments.add(t);
			}
		}
		return arguments;
	}

	private static String parseCalcPV(String pvName) {
		try {
			String most = pvName.substring(6);
			int firstParen = most.indexOf('(');
			String ops = most.substring(0, firstParen);
			String args = most.substring(firstParen + 1, most.length() - 1);
			List<String> operators = parseOperator(ops);
			operators.get(0);
			List<String> arguments = parseArguments(args);
			for (int i = 0; i < operators.size(); i++) {
				String op = operators.get(i);
				if (StringUtils.isNumeric(op)) {
					System.out.println("Adding " + op + " to index " + i / 2);
					arguments.add(i / 2, op);
					operators.remove(i);
					// We've removed an item from the list while iterating
					i--;
				}
			}

			String output = "=(";
			for (int i = 0; i < arguments.size() - 1; i++) {
				output += arguments.get(i);
				output += operators.get(i);
			}
			output += arguments.get(arguments.size() - 1);
			output += ")";
			return output;
		} catch (Exception e) {
			return pvName;
		}
	}

	private static boolean isPvString(String argument) {
		return !StringUtils.isNumeric(argument);
	}

}
