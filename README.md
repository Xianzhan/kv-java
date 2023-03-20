# kv

    一个 Java 实现的 kv 存储引擎。
    kv 存储引擎底层主要有几种数据结构：bitcask, LSM Tree, B Tree.

https://github.com/Leviathan1995/Atendb
https://github.com/douban/beansdb
https://github.com/weicao/cascadb

https://codecapsule.com/2012/11/07/ikvs-implementing-a-key-value-store-table-of-contents/

# 数据结构

- 哈希存储：使用哈希函数将键映射到值，适合快速查找和插入，但不支持范围查询和排序。
- B树、B+树、B*树：使用多路平衡搜索树来组织数据，适合范围查询和排序，但需要维护平衡性和节点分裂。
- LSM树：使用日志结构合并树来组织数据，适合写入密集的场景，但读取性能较低。
- R树：使用多维空间索引来组织数据，适合空间相关的查询，如地理位置、区域等。
- 倒排索引：使用文档中出现的词作为键，文档的标识符作为值，适合全文检索和关键词搜索。
- 矩阵存储：使用二维数组或稀疏矩阵来组织数据，适合数值计算和分析。
- 对象与块：使用对象或块作为基本单位来存储数据，适合面向对象或文件系统等场景。
- 图结构存储：使用节点和边来表示实体和关系，适合图数据库或社交网络等场景。