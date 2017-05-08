package cn.sinjinsong.skeleton.security.token;

/**
 * Created by SinjinSong on 2017/4/27.
 */
public interface TokenManager {
    /**
     * 根据查询出来的用户username生成token，并将该token和用户id放入缓存
     * @param username
     * @return
     */
    String createToken(String username);

    /**
     * 检查token是否有效，如果token格式正确并且尚未过期，那么从Redis缓存中取出id
     * @param token
     * @return
     */
    String checkToken(String token);
    
    /**
     * 当登出后，删除token
     * @param token
     */
    void deleteToken(String token);
}
