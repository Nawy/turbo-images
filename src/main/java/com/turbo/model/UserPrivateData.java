package com.turbo.model;

import com.google.common.net.InetAddresses;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by rakhmetov on 09.05.17.
 * <p>
 * This data is shown to user only!!!
 */
@Table("user_private")
public class UserPrivateData {

    @PrimaryKey
    private Long id;
    private long userPublicDataId; // user public information record identificator

    private long[] upvotedPostsIds; // some other posts that upvoted by user
    private long[] likedPostIds; // some other posts what liked by user

    private InetAddresses lastIp; // last lastIp from what was came in
    private InetAddresses currentIp;
    private String token; //password hash

    public UserPrivateData() {
    }

    public UserPrivateData(
            Long id,
            long userPublicDataId,
            long[] upvotedPostsIds,
            long[] likedPostIds,
            InetAddresses lastIp,
            InetAddresses currentIp,
            String token
    ) {
        this.id = id;
        this.userPublicDataId = userPublicDataId;
        this.upvotedPostsIds = upvotedPostsIds;
        this.likedPostIds = likedPostIds;
        this.lastIp = lastIp;
        this.currentIp = currentIp;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public long getUserPublicDataId() {
        return userPublicDataId;
    }

    public long[] getUpvotedPostsIds() {
        return upvotedPostsIds;
    }

    public long[] getLikedPostIds() {
        return likedPostIds;
    }

    public InetAddresses getLastIp() {
        return lastIp;
    }

    public InetAddresses getCurrentIp() {
        return currentIp;
    }

    public String getToken() {
        return token;
    }
}
