{- Grupo: X
   Integrante(s):
     Guani, Joaquin, 5.334.276-4
     Kasek, Lukas, 5.446.964-2
-}

module JSONLibrary
 (actualizarobjeto, lookupField,
  lookupFieldObj,
  keysOf,
  valuesOf,
  entriesOf,
  leftJoin,
  rightJoin,
  filterArray,
  insertKV,
  sortKeys,
  mkJString, mkJNumber, mkJBoolean, mkJNull, mkJObject, mkJArray,
  fromJString, fromJNumber, fromJBoolean, fromJObject, fromJArray,
  isJString, isJNumber, isJBoolean, isJNull, isJObject, isJArray,
  JSON(),importJSON,
  Object()
 )
where

import AST

actualizarobjeto :: JSON -> String -> JSON -> JSON
actualizarobjeto (JObject j) key nuevo =
  JObject (map reemplazar j)
  where
    reemplazar (k, v) =
      if k == key then (k, nuevo) else (k, v)

{- lookupField:
 Cuando el primer argumento es un objeto y tiene como clave el valor
 dado como segundo argumento, entonces se retorna el valor JSON
 correspondiente (bajo el constructor {\tt Just}). De lo contrario se
 retorna {\tt Nothing}. Si un objeto tiene claves repetidas, se
 retorna el valor de más a la derecha.
-}
lookupField :: JSON -> Key -> Maybe JSON
lookupField (JObject obj) key
  | null coincidencias = Nothing
  | otherwise          = Just (snd (last coincidencias))
  where
    coincidencias = filter (\(k, _) -> k == key) obj
lookupField _ _ = Nothing

-- Análoga a la anterior, pero el primer argumento es un objeto.
lookupFieldObj :: Object JSON -> Key -> Maybe JSON
lookupFieldObj obj key
  | null coincidencias = Nothing
  | otherwise          = Just (snd (last coincidencias))
  where
    coincidencias = filter (\(k,_) -> k== key ) obj

-- retorna la lista de claves de un objeto, manteniendo el orden en el
-- que se encontraban.
keysOf :: Object JSON -> [Key]
keysOf = map fst

-- Retorna una lista con los valores contenidos en los campos de un objeto,
-- manteniendo el orden en el que se encontraban.
valuesOf :: Object JSON -> [JSON]
valuesOf = map snd

-- retorna todos los campos de un objeto, en el orden en que se encontraban.
entriesOf :: Object JSON -> [(Key,JSON)]
entriesOf o = o

keysOfObjet :: Object a -> [String]
keysOfObjet = map fst

-- Se combinan dos objetos, en orden.  En caso que haya claves
-- repetidas en ambos objetos, en la unión tienen prioridad los
-- campos del primer objeto.
leftJoin :: Object a -> Object a -> Object a
leftJoin ob1 ob2= leftJoinUltra (keysOfObjet ob1) ob1 ob2
leftJoinUltra a (x:xs) ob2 = x : leftJoinUltra a xs ob2
leftJoinUltra a [] (x:xs) = if (elem (fst x) a) then leftJoinUltra a [] xs
                            else x : leftJoinUltra a [] xs
leftJoinUltra a [] [] = []


-- Se combinan dos objetos, en orden.  En caso que haya claves
-- repetidas en ambos objetos, en la unión tienen prioridad los
-- campos del segundo objeto.
rightJoin :: Object a -> Object a -> Object a
rightJoin ob1 ob2= rightJoinUltra (keysOfObjet ob2) ob1 ob2
rightJoinUltra a (x:xs) ob2 = if (elem (fst x) a) then rightJoinUltra a xs ob2
                            else x : rightJoinUltra a xs ob2
rightJoinUltra a [] (x:xs) = x : rightJoinUltra a [] xs
rightJoinUltra a [] [] = []


-- Dado un predicado sobre objetos JSON, y un arreglo, construye el
-- arreglo con los elementos que satisfacen el predicado.
filterArray :: (JSON -> Bool) ->  Array -> Array
filterArray f [] = []
filterArray f (x:xs) =  if f x then x : filterArray f xs
                          else filterArray f xs

-- Se inserta un campo en un objeto. Si las claves del objeto están
-- ordenadas lexicográficamente, el resultado debe conservar esta
-- propiedad.
insertKV :: (Key, v) -> Object v -> Object v
insertKV (a, v) [] = (a, v) : []
insertKV (a, v) (x : xs) = if (a < fst x) then (a, v) : x : xs
                            else x : insertKV (a, v) xs

-- Se inserta un campo en un objeto, al inicio
consKV :: (Key, v) -> Object v -> Object v
consKV (c, v) xs = (c, v) : xs

-- ordena claves de un objeto
sortKeys :: Object a -> Object a
sortKeys = foldr insertKV []


-- constructoras
mkJString :: String -> JSON
mkJString = JString

mkJNumber :: Integer -> JSON
mkJNumber = JNumber

mkJBoolean :: Bool -> JSON
mkJBoolean = JBoolean

mkJNull :: () -> JSON
mkJNull () = JNull

mkJArray :: [JSON] -> JSON
mkJArray = JArray

mkJObject :: [(Key, JSON)] -> JSON
mkJObject = JObject


-- destructoras
fromJString :: JSON -> Maybe String
fromJString (JString s) = Just s
fromJString _ = Nothing

fromJNumber :: JSON -> Maybe Integer
fromJNumber (JNumber n) = Just n
fromJNumber _ = Nothing

fromJBoolean  :: JSON -> Maybe Bool
fromJBoolean (JBoolean b) = Just b
fromJBoolean _ = Nothing 

fromJObject :: JSON -> Maybe (Object JSON)
fromJObject (JObject obj) = Just obj
fromJObject _ = Nothing 

fromJArray :: JSON -> Maybe [JSON]
fromJArray (JArray a ) = Just a
fromJArray _ = Nothing


-- predicados
isJNumber :: JSON -> Bool
isJNumber (JNumber n) = True
isJNumber _ = False

isJNull :: JSON -> Bool
isJNull JNull = True
isJNull _ = False

isJString :: JSON -> Bool
isJString (JString h) = True
isJString _ = False

isJObject :: JSON -> Bool
isJObject (JObject a) = True
isJObject _ = False

isJArray :: JSON -> Bool
isJArray (JArray a) = True
isJArray _ = False

isJBoolean :: JSON -> Bool
isJBoolean (JBoolean b)= True
isJBoolean _ = False

