package com.yujie.hero.data.bean;

public class Result {
	private boolean Flag;
	private String msg;
	public Result() {
		super();
	}
	public Result(boolean Flag, String msg) {
		super();
		this.Flag = Flag;
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Result [isSuccess=" + Flag + ", msg=" + msg + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (Flag ? 1231 : 1237);
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (Flag != other.Flag)
			return false;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		return true;
	}
	public boolean isFlag() {
		return Flag;
	}
	public void setFlag(boolean flag) {
		Flag = flag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
