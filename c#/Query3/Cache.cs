using System;
using System.Runtime.Caching;

namespace queries{
    // Cache global que almacena los datos
    class Cache{
        public static System.Runtime.Caching.MemoryCache memoriaCache = new System.Runtime.Caching.MemoryCache("Query3");

        public string getInfoCache(string llave){
            return memoriaCache.Get(llave) as string;
        }

        public void agregarInfoACache(string llave, string info){
            memoriaCache.Set(llave, getInfoCache(llave) + info, new CacheItemPolicy(){
                AbsoluteExpiration = DateTime.Now.AddDays(1)
            });
        }
    }
}