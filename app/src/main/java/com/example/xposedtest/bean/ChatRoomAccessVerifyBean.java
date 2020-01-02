package com.example.xposedtest.bean;

import java.util.List;

public class ChatRoomAccessVerifyBean {

    /**
     * link : {"content":"\n\t\t","memberlist":{"content":"\n\t\t\t","memberlistsize":1,"member":[{"content":"\n\t\t\t\t","username":"wxid_sld0oxm1l19r22","nickname":"A封阳台（刘伟）","headimgurl":"http://wx.qlogo.cn/mmhead/ver_1/zY3PNUIDC4Tqw9jIiaaJ2YM0yic5EPr7cibameXNlnatjWbyL2O3qH8qotvfMZU0oE8fMp0ZfNjUl7CG6wicXFbAf4ewicibqoI6lFuWsyN2pAnQI/132"}]},"text":"  去确认","ticket":"AQAAAAEAAAAdD9c45ZokyVUpILPrMtqqs+Cf2cbrXNRK5XtmEzAd3eMqfTK6PqEBkaP0Tvso9XdBDZ74VQ7crzLInmauBQ03+cue/Q==","inviterusername":"wxid_lqqaa0nykwhl21","invitationreason":"Pppp","scene":"roomaccessapplycheck_approve"}
     */

    private LinkBean link;

    public LinkBean getLink() {
        return link;
    }

    public void setLink(LinkBean link) {
        this.link = link;
    }

    public static class LinkBean {
        /**
         * content :
         * memberlist : {"content":"\n\t\t\t","memberlistsize":1,"member":[{"content":"\n\t\t\t\t","username":"wxid_sld0oxm1l19r22","nickname":"A封阳台（刘伟）","headimgurl":"http://wx.qlogo.cn/mmhead/ver_1/zY3PNUIDC4Tqw9jIiaaJ2YM0yic5EPr7cibameXNlnatjWbyL2O3qH8qotvfMZU0oE8fMp0ZfNjUl7CG6wicXFbAf4ewicibqoI6lFuWsyN2pAnQI/132"}]}
         * text :   去确认
         * ticket : AQAAAAEAAAAdD9c45ZokyVUpILPrMtqqs+Cf2cbrXNRK5XtmEzAd3eMqfTK6PqEBkaP0Tvso9XdBDZ74VQ7crzLInmauBQ03+cue/Q==
         * inviterusername : wxid_lqqaa0nykwhl21
         * invitationreason : Pppp
         * scene : roomaccessapplycheck_approve
         */

        private MemberlistBean memberlist;
        private String ticket;
        private String inviterusername;
        private String invitationreason;

        public MemberlistBean getMemberlist() {
            return memberlist;
        }

        public void setMemberlist(MemberlistBean memberlist) {
            this.memberlist = memberlist;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public String getInviterusername() {
            return inviterusername;
        }

        public void setInviterusername(String inviterusername) {
            this.inviterusername = inviterusername;
        }

        public String getInvitationreason() {
            return invitationreason;
        }

        public void setInvitationreason(String invitationreason) {
            this.invitationreason = invitationreason;
        }

        public static class MemberlistBean {
            /**
             * content :
             * memberlistsize : 1
             * member : [{"content":"\n\t\t\t\t","username":"wxid_sld0oxm1l19r22","nickname":"A封阳台（刘伟）","headimgurl":"http://wx.qlogo.cn/mmhead/ver_1/zY3PNUIDC4Tqw9jIiaaJ2YM0yic5EPr7cibameXNlnatjWbyL2O3qH8qotvfMZU0oE8fMp0ZfNjUl7CG6wicXFbAf4ewicibqoI6lFuWsyN2pAnQI/132"}]
             */

            private int memberlistsize;
            private List<MemberBean> member;

            public int getMemberlistsize() {
                return memberlistsize;
            }

            public void setMemberlistsize(int memberlistsize) {
                this.memberlistsize = memberlistsize;
            }

            public List<MemberBean> getMember() {
                return member;
            }

            public void setMember(List<MemberBean> member) {
                this.member = member;
            }

            public static class MemberBean {
                /**
                 * content :
                 * username : wxid_sld0oxm1l19r22
                 * nickname : A封阳台（刘伟）
                 * headimgurl : http://wx.qlogo.cn/mmhead/ver_1/zY3PNUIDC4Tqw9jIiaaJ2YM0yic5EPr7cibameXNlnatjWbyL2O3qH8qotvfMZU0oE8fMp0ZfNjUl7CG6wicXFbAf4ewicibqoI6lFuWsyN2pAnQI/132
                 */

                private String username;
                private String nickname;
                private String headimgurl;

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getHeadimgurl() {
                    return headimgurl;
                }

                public void setHeadimgurl(String headimgurl) {
                    this.headimgurl = headimgurl;
                }
            }
        }
    }
}
