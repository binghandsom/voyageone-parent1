/**
 * Created by linanbin on 15/11/24.
 */

module.exports = {
    "/cms/common/menu/doGetTest": function (req, res) {
        if (req.name == "1") {
            return res.render("cms/common/menu/doGetTest.json");
        } else if (req.name == "2") {
            return res.render("cms/common/menu/doGetTestError.json");
        }

    }
};