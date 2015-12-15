package org.kiwi.crius.tools.meta.mapping.oom;

/**
 * @author xujun  --xujun@damai.cn
 *
 */
public abstract class AbstractFetchInfo implements Fetch {

	private String name;
	private String comment;
	private String identifier;
	private String creator;
	private String createDate;

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getCreator() {
		return creator;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
