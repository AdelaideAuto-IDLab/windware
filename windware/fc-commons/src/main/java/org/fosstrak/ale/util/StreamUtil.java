/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org).
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.fosstrak.ale.util;

import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * This class provides a method to convert an input stream into a string.
 * 
 * @author regli
 */
public final class StreamUtil {
	
	/** logger. */
	private static final Logger LOG = Logger.getLogger(StreamUtil.class);

	/**
	 * This method converts an input stream into a string.
	 * 
	 * @param in to convert
	 * @return string
	 */
	public static String inputStream2String(InputStream in) {		
		try {			
			StringBuffer buf = new StringBuffer();
			while (in.available() > 0) {
				int i = in.read();
				buf.append((char)i);
			}
			return buf.toString();
		} catch (Exception e) {
			LOG.error("could not convert input stream to string - returning null.", e);	
		}
		return null;
	}	
}