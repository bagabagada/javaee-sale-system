-- Users (password: 123456)
INSERT INTO `sys_user` (`username`, `password`, `nickname`, `balance`, `role`) VALUES 
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 10000.00, 1),
('user1', 'e10adc3949ba59abbe56e057f20f883e', '用户一', 10000.00, 0),
('user2', 'e10adc3949ba59abbe56e057f20f883e', '用户二', 10000.00, 0),
('user3', 'e10adc3949ba59abbe56e057f20f883e', '用户三', 10000.00, 0);

-- Goods
INSERT INTO `goods` (`name`, `title`, `img`, `detail`, `category`, `price`, `stock`) VALUES 
('iPhone 15 Pro', 'Apple iPhone 15 Pro Max 256GB 蓝色钛金属', 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-15-pro-finish-select-202309-6-1inch-bluetitanium', 'A17 Pro 芯片，钛金属设计。', '数码', 9999.00, 100),
('MacBook Pro', 'Apple MacBook Pro 14英寸 M3 芯片', 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/mbp14-spacegray-select-202110', '性能强劲，令人惊叹。', '电脑', 12999.00, 50),
('Nintendo Switch', 'Nintendo Switch OLED 游戏主机 - 白色', 'https://assets.nintendo.com/image/upload/f_auto/q_auto/dpr_1.5/c_scale,w_600/ncom/en_US/switch/site-design-update/hardware/switch/oled-model/gallery/white/01', '7英寸 OLED 屏幕。', '游戏', 2599.00, 200),
('Sony PS5', 'PlayStation 5 游戏主机 (轻薄版)', 'https://gmedia.playstation.com/is/image/SIEPDC/ps5-slim-disc-console-image-block-01-en-16nov23?$1600px$', '玩无极限。', '游戏', 3599.00, 80),
('iPad Air 5', 'Apple iPad Air (第5代) M1 芯片', 'https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/ipad-air-finish-select-gallery-202211-blue-wifi', '轻薄，多彩，实力出众。', '数码', 4799.00, 150),
('Huawei Mate 60 Pro', '华为 Mate 60 Pro 12GB+512GB 雅川青', 'https://consumer.huawei.com/content/dam/huawei-cbg-site/common/mkt/pdp/phones/mate60-pro/images/sec1/mate60-pro-kv.jpg', '卫星通话，昆仑玻璃。', '数码', 6999.00, 50),
('Xiaomi 14', '小米 14 徕卡光学镜头 骁龙8 Gen 3', 'https://i01.appmifile.com/v1/MI_18455B3E4DA706226CF7535A58E875F0267/pms_1698307633.98596645.png', '小尺寸，Pro级影像。', '数码', 3999.00, 100),
('Dyson Hair Dryer', '戴森 Supersonic 吹风机 HD15', 'https://dyson-h.assetsadobe2.com/is/image/content/dam/dyson/images/products/hair-care/dyson-supersonic-hair-dryer/hd15-ceramic-pop/605x420/DS_605x420_V3_Ceramic_Pop.jpg', '快速干发，无惧高温。', '家居', 2999.00, 30),
('Lego Ferrari', '乐高 机械组 法拉利 Daytona SP3 42143', 'https://www.lego.com/cdn/cs/set/assets/blt0db7653765275822/42143_alt1.png', '拼搭属于你的超跑。', '游戏', 3499.00, 20);

-- Seckill Activities (Ongoing & Upcoming)
INSERT INTO `seckill_activity` (`goods_id`, `seckill_price`, `stock_count`, `start_time`, `end_time`, `status`) VALUES 
(1, 7999.00, 10, '2024-01-01 00:00:00', '2030-12-31 00:00:00', 1), -- Ongoing (iPhone)
(2, 9999.00, 5, '2024-01-01 00:00:00', '2030-12-31 00:00:00', 1), -- Ongoing (MacBook)
(3, 1999.00, 50, '2024-01-01 00:00:00', '2030-12-31 00:00:00', 1), -- Ongoing (Switch)
(4, 2999.00, 20, '2024-01-01 00:00:00', '2030-12-31 00:00:00', 1), -- Ongoing (PS5)
(5, 3999.00, 10, '2024-01-01 00:00:00', '2030-12-31 00:00:00', 1), -- Ongoing (iPad)
(6, 5999.00, 5, '2024-01-01 00:00:00', '2030-12-31 00:00:00', 1), -- Ongoing (Huawei)
(7, 3499.00, 20, '2024-01-01 00:00:00', '2030-12-31 00:00:00', 1), -- Ongoing (Xiaomi)
(8, 1999.00, 5, '2024-01-01 00:00:00', '2030-12-31 00:00:00', 1), -- Ongoing (Dyson)
(9, 2999.00, 10, '2024-01-01 00:00:00', '2030-12-31 00:00:00', 1); -- Ongoing (Lego)
