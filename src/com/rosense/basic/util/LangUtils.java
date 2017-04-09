package com.rosense.basic.util;
/**
 * 
 * @author Can-Dao
 * 	
 * 2015年8月18日 下午8:21:52
 *
 */
public final class LangUtils {
	public static final Integer toInt(final Object obj) {
		if (obj == null) {
			return null;
		}

		try {
			if (obj instanceof Number) {
				return new Integer(((Number) obj).intValue());
			}
			if (obj instanceof Boolean) {
				return obj.equals(Boolean.FALSE) ? new Integer(0) : new Integer(-1);
			}

			return Integer.valueOf(obj.toString().trim());
		} catch (final Throwable t) {
			try {
				return Integer.valueOf(obj.toString().trim());
			} catch (final Throwable ta) {
				// ignore
			}
		}
		return null;
	}

	public static final int[] toInt(final String[] arr) {
		final int[] ii = new int[arr.length];
		for (int i = 0; i < ii.length; i++) {
			ii[i] = Integer.parseInt(arr[i]);
		}
		return ii;
	}

	public static final int toInt(final Object obj, final int defaultValue) {
		if (obj == null)
			return defaultValue;
		try {
			if (obj instanceof Number)
				return ((Number) obj).intValue();
			if (obj instanceof Boolean)
				return (Boolean) obj ? 0 : -1;
			return Integer.parseInt(obj.toString());
		} catch (final Throwable t) {
			return defaultValue;
		}
	}

	public static final Long toLong(final Object obj) {
		if (obj instanceof Long) {
			return (Long) obj;
		}
		if (obj instanceof Number) {
			return new Long(((Number) obj).longValue());
		}
		if (obj instanceof java.util.Date) {
			return new Long(((java.util.Date) obj).getTime());
		}
		if (obj instanceof java.sql.Timestamp) {
			return new Long(((java.sql.Timestamp) obj).getTime());
		}
		try {
			return new Long(Long.parseLong(obj.toString()));
		} catch (final Throwable t) {
		}
		return null;
	}

	public static final long toLong(final Object obj, final long defaultValue) {
		final Long l = LangUtils.toLong(obj);
		return l == null ? defaultValue : l.longValue();
	}

	public static final Double toDouble(final Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			if (obj instanceof Number) {
				return new Double(((Number) obj).doubleValue());
			}
			if (obj instanceof Boolean) {
				return obj.equals(Boolean.FALSE) ? new Double(0.0) : new Double(-1.0);
			}

			return Double.valueOf(obj.toString());
		} catch (final Throwable t) {
		}
		return Double.valueOf(0);
	}

	public static final double toDouble(final Object obj, final double defaultValue) {
		final Double d = LangUtils.toDouble(obj);
		return d == null ? defaultValue : d.doubleValue();
	}

	public static final float toFloat(final Object obj, final float defauleValue) {
		return (float) LangUtils.toDouble(obj, defauleValue);
	}

	public static final Byte toByte(final Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Number) {
			return new Byte(((Number) obj).byteValue());
		}
		if (obj instanceof Boolean) {
			return obj.equals(Boolean.FALSE) ? new Byte((byte) 0) : new Byte((byte) -1);
		}
		try {
			return Byte.valueOf(obj.toString());
		} catch (final Throwable t) {
		}
		return null;
	}

	public static final byte toByte(final Object obj, final byte defaultValue) {
		if (obj == null)
			return defaultValue;
		try {
			if (obj instanceof Number)
				return ((Number) obj).byteValue();
			if (obj instanceof Boolean)
				return (byte) ((Boolean) obj ? 0 : -1);
			return Byte.parseByte(obj.toString());
		} catch (final Throwable t) {
			return defaultValue;
		}
	}

	public static final Short toShort(final Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Number) {
			return new Short(((Number) obj).shortValue());
		}
		if (obj instanceof Boolean) {
			return obj.equals(Boolean.FALSE) ? new Short((short) 0) : new Short((short) -1);
		}
		try {
			return Short.valueOf(obj.toString());
		} catch (final Throwable t) {
		}
		return null;
	}

	public static final short toShort(final Object obj, final short defaultValue) {
		if (obj == null)
			return defaultValue;
		try {
			if (obj instanceof Number)
				return ((Number) obj).shortValue();
			if (obj instanceof Boolean)
				return (short) ((Boolean) obj ? 0 : -1);
			return Short.parseShort(obj.toString());
		} catch (final Throwable t) {
			return defaultValue;
		}
	}

	public static final Boolean toBoolean(final Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof Number) {
			return ((Number) obj).intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
		} else if (obj instanceof String) {
			final String s = (String) obj;
			if (s.equalsIgnoreCase("true")) {
				return Boolean.TRUE;
			} else if (s.equalsIgnoreCase("false")) {
				return Boolean.FALSE;
			} else {
				try {
					return new Boolean(Integer.parseInt((String) obj) != 0);
				} catch (final Throwable t) {
					return Boolean.FALSE;
				}
			}
		}
		return null;
	}

	public static final boolean toBoolean(final Object value, final boolean defaultValue) {
		final Boolean v = LangUtils.toBoolean(value);
		return v == null ? defaultValue : v.booleanValue();
	}
}
